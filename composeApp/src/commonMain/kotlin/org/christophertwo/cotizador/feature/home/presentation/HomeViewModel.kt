package org.christophertwo.cotizador.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.christophertwo.cotizador.data.Product
import org.christophertwo.cotizador.data.YazbekService
import org.christophertwo.cotizador.feature.home.domain.ProductAttributeExtractor

class HomeViewModel : ViewModel() {

    private var hasLoadedInitialData = false
    private val yazbekService = YazbekService()

    private val _state = MutableStateFlow(HomeState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadYazbekData()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HomeState()
        )

    private fun loadYazbekData() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true, error = null) }

                val yazbekData = yazbekService.loadFromResources()

                // Extraer todos los productos de todas las categorías
                val allProducts = yazbekData.catalogo.flatMap { category ->
                    category.productos
                }

                // Extraer categorías disponibles
                val categories = yazbekData.catalogo.map { it.categoria }

                // Extraer atributos disponibles de todos los productos
                val neckTypes = ProductAttributeExtractor.getAvailableValues(allProducts, "cuello")
                val sleeveTypes = ProductAttributeExtractor.getAvailableValues(allProducts, "manga")
                val genders = ProductAttributeExtractor.getAvailableValues(allProducts, "genero")
                val characteristics = ProductAttributeExtractor.getAvailableValues(allProducts, "caracteristica")

                _state.update {
                    it.copy(
                        isLoading = false,
                        yazbekData = yazbekData,
                        allProducts = allProducts,
                        filteredProducts = allProducts,
                        availableCategories = categories,
                        availableNeckTypes = neckTypes,
                        availableSleeveTypes = sleeveTypes,
                        availableGenders = genders,
                        availableCharacteristics = characteristics
                    )
                }

                // Cargar la primera página automáticamente
                loadPage(0, allProducts)
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar los datos: ${e.message}"
                    )
                }
            }
        }
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.UpdateSearchQuery -> {
                _state.update { it.copy(searchQuery = action.query) }
                applyFilters()
            }

            HomeAction.PerformSearch -> {
                // La búsqueda se hace en tiempo real con UpdateSearchQuery
            }

            is HomeAction.SetSearchActive -> _state.update { it.copy(isSearchActive = action.isActive) }

            HomeAction.LoadMoreProducts -> {
                val nextPage = _state.value.currentPage + 1
                loadPage(nextPage, _state.value.filteredProducts)
            }

            HomeAction.ToggleFilterDialog -> {
                _state.update { it.copy(isFilterDialogOpen = !it.isFilterDialogOpen) }
            }

            is HomeAction.SelectCategory -> {
                _state.update { it.copy(selectedCategory = action.category) }
                applyFilters()
            }

            is HomeAction.SetPriceRange -> {
                _state.update {
                    it.copy(
                        minPriceFilter = action.min,
                        maxPriceFilter = action.max
                    )
                }
                applyFilters()
            }

            HomeAction.ClearFilters -> {
                _state.update {
                    it.copy(
                        selectedCategory = null,
                        minPriceFilter = null,
                        maxPriceFilter = null,
                        searchQuery = ""
                    )
                }
                applyFilters()
            }

            else -> {}
        }
    }

    private fun applyFilters() {
        val currentState = _state.value
        val searchQuery = currentState.searchQuery
        val selectedCategory = currentState.selectedCategory
        val minPrice = currentState.minPriceFilter
        val maxPrice = currentState.maxPriceFilter
        val selectedNeckType = currentState.selectedNeckType
        val selectedSleeveType = currentState.selectedSleeveType
        val selectedGender = currentState.selectedGender
        val selectedCharacteristic = currentState.selectedCharacteristic

        var filtered = currentState.allProducts

        // Filtrar por búsqueda
        if (searchQuery.isNotEmpty()) {
            val searchWords = searchQuery.trim().split(Regex("\\s+"))
                .filter { it.isNotEmpty() }

            filtered = filtered.filter { product ->
                searchWords.all { word ->
                    product.codigo.contains(word, ignoreCase = true) ||
                    product.descripcion.contains(word, ignoreCase = true)
                }
            }
        }

        // Filtrar por categoría
        if (selectedCategory != null) {
            val categoryProducts = currentState.yazbekData?.catalogo
                ?.find { it.categoria == selectedCategory }
                ?.productos ?: emptyList()

            filtered = filtered.filter { product ->
                categoryProducts.any { it.codigo == product.codigo }
            }
        }

        // Filtrar por atributos de descripción
        if (selectedNeckType != null) {
            filtered = filtered.filter { product ->
                ProductAttributeExtractor.extractAttributes(product.descripcion)
                    .any { it.key == "cuello" && it.value == selectedNeckType }
            }
        }

        if (selectedSleeveType != null) {
            filtered = filtered.filter { product ->
                ProductAttributeExtractor.extractAttributes(product.descripcion)
                    .any { it.key == "manga" && it.value == selectedSleeveType }
            }
        }

        if (selectedGender != null) {
            filtered = filtered.filter { product ->
                ProductAttributeExtractor.extractAttributes(product.descripcion)
                    .any { it.key == "genero" && it.value == selectedGender }
            }
        }

        if (selectedCharacteristic != null) {
            filtered = filtered.filter { product ->
                ProductAttributeExtractor.extractAttributes(product.descripcion)
                    .any { it.key == "caracteristica" && it.value == selectedCharacteristic }
            }
        }

        // Filtrar por rango de precio
        if (minPrice != null || maxPrice != null) {
            filtered = filtered.filter { product ->
                val productMinPrice = product.precios.minOfOrNull {
                    minOf(it.precioMayoreo, it.precioMenudeo)
                } ?: 0.0

                val matchesMin = minPrice == null || productMinPrice >= minPrice
                val matchesMax = maxPrice == null || productMinPrice <= maxPrice

                matchesMin && matchesMax
            }
        }

        _state.update { it.copy(filteredProducts = filtered) }
        loadPage(0, filtered)
    }

    private fun loadPage(page: Int, sourceProducts: List<Product>) {
        val pageSize = _state.value.pageSize
        val startIndex = page * pageSize
        val endIndex = minOf(startIndex + pageSize, sourceProducts.size)

        if (startIndex < sourceProducts.size) {
            val newProducts = sourceProducts.subList(startIndex, endIndex)
            val displayedProducts = if (page == 0) {
                newProducts
            } else {
                _state.value.displayedProducts + newProducts
            }

            _state.update {
                it.copy(
                    displayedProducts = displayedProducts,
                    currentPage = page,
                    hasMoreProducts = endIndex < sourceProducts.size
                )
            }
        }
    }

}
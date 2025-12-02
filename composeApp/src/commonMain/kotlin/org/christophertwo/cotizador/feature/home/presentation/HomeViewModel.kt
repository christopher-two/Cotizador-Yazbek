package org.christophertwo.cotizador.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.christophertwo.cotizador.data.Product
import org.christophertwo.cotizador.data.YazbekService

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

                _state.update {
                    it.copy(
                        isLoading = false,
                        yazbekData = yazbekData,
                        allProducts = allProducts,
                        filteredProducts = allProducts
                    )
                }
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
                val newQuery = action.query
                val filtered = if (newQuery.isEmpty()) {
                    _state.value.allProducts
                } else {
                    // Dividir la búsqueda en palabras individuales
                    val searchWords = newQuery.trim().split(Regex("\\s+"))
                        .filter { it.isNotEmpty() }

                    _state.value.allProducts.filter { product ->
                        // El producto coincide si TODAS las palabras de búsqueda
                        // están en el código O en la descripción
                        searchWords.all { word ->
                            product.codigo.contains(word, ignoreCase = true) ||
                            product.descripcion.contains(word, ignoreCase = true)
                        }
                    }
                }
                _state.update { it.copy(searchQuery = newQuery, filteredProducts = filtered) }

                // Resetear paginación y cargar primera página con los resultados filtrados
                loadPage(0, filtered)
            }

            HomeAction.PerformSearch -> {
                // La búsqueda se hace en tiempo real con UpdateSearchQuery
            }

            is HomeAction.SetSearchActive -> _state.update { it.copy(isSearchActive = action.isActive) }

            HomeAction.LoadMoreProducts -> {
                val nextPage = _state.value.currentPage + 1
                loadPage(nextPage, _state.value.filteredProducts)
            }
        }
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
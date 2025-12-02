package org.christophertwo.cotizador.feature.home.presentation

import org.christophertwo.cotizador.data.Product
import org.christophertwo.cotizador.data.YazbekPriceList

data class HomeState(
    val searchQuery: String = "",
    val isSearchActive: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null,
    val yazbekData: YazbekPriceList? = null,
    val allProducts: List<Product> = emptyList(),
    val filteredProducts: List<Product> = emptyList(),
    val displayedProducts: List<Product> = emptyList(), // Productos mostrados con paginación
    val currentPage: Int = 0,
    val pageSize: Int = 20,
    val hasMoreProducts: Boolean = false,
    // Filtros básicos
    val availableCategories: List<String> = emptyList(),
    val selectedCategory: String? = null,
    val minPriceFilter: Double? = null,
    val maxPriceFilter: Double? = null,
    val isFilterDialogOpen: Boolean = false,
    // Filtros de atributos dinámicos
    val selectedNeckType: String? = null, // cuello
    val selectedSleeveType: String? = null, // manga
    val selectedGender: String? = null, // genero
    val selectedCharacteristic: String? = null, // caracteristica
    val availableNeckTypes: List<String> = emptyList(),
    val availableSleeveTypes: List<String> = emptyList(),
    val availableGenders: List<String> = emptyList(),
    val availableCharacteristics: List<String> = emptyList()
)
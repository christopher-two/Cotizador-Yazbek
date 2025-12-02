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
    val hasMoreProducts: Boolean = false
)
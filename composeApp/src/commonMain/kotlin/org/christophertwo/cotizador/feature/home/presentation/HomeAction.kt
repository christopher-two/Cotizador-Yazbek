package org.christophertwo.cotizador.feature.home.presentation

sealed interface HomeAction {
    data class UpdateSearchQuery(val query: String) : HomeAction
    data object PerformSearch : HomeAction
    data class SetSearchActive(val isActive: Boolean) : HomeAction
    data object LoadMoreProducts : HomeAction
    data object ToggleFilterDialog : HomeAction
    data class SelectCategory(val category: String?) : HomeAction
    data class SetPriceRange(val min: Double?, val max: Double?) : HomeAction
    data object ClearFilters : HomeAction
    // Filtros de atributos
    data class SelectNeckType(val neckType: String?) : HomeAction
    data class SelectSleeveType(val sleeveType: String?) : HomeAction
    data class SelectGender(val gender: String?) : HomeAction
    data class SelectCharacteristic(val characteristic: String?) : HomeAction
}
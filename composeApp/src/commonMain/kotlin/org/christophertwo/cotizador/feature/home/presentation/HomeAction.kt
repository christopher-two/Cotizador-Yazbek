package org.christophertwo.cotizador.feature.home.presentation

sealed interface HomeAction {
    data class UpdateSearchQuery(val query: String) : HomeAction
    object PerformSearch : HomeAction
    data class SetSearchActive(val isActive: Boolean) : HomeAction
    object LoadMoreProducts : HomeAction
}
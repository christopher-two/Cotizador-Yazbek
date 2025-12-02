package org.christophertwo.cotizador.core.common

import kotlinx.serialization.Serializable

@Serializable
sealed class RouteScreen {
    @Serializable
    data object Home : RouteScreen()
    @Serializable
    data class Quote(val productCode: String) : RouteScreen()
}
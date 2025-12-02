package org.christophertwo.cotizador.feature.quote.domain

data class PrintConfig(
    val position: Int, // 1-4
    val price: Double = 0.0
)


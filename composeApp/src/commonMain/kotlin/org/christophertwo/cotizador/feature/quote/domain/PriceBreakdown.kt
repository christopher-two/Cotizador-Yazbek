package org.christophertwo.cotizador.feature.quote.domain

data class PriceBreakdown(
    val basePrice: Double,
    val basePriceExplanation: String,
    val totalPrintsCost: Double,
    val printsExplanation: String,
    val subtotalWithoutProfit: Double,
    val profitAmount: Double,
    val profitExplanation: String,
    val finalPricePerUnit: Double,
    val quantity: Int,
    val totalFinal: Double
)


package org.christophertwo.cotizador.feature.quote.presentation

import org.christophertwo.cotizador.data.Product
import org.christophertwo.cotizador.feature.quote.domain.PrintConfig
import org.christophertwo.cotizador.feature.quote.domain.PriceBreakdown
import org.christophertwo.cotizador.feature.quote.domain.SaleType

data class QuoteState(
    val product: Product? = null,
    val saleType: SaleType = SaleType.MAYOREO,
    val selectedColor: String? = null,
    val selectedSize: String? = null,
    val printConfigs: List<PrintConfig> = emptyList(),
    val quantity: Int = 1,
    val profitMargin: Float = 0f, // 0-100
    val priceBreakdown: PriceBreakdown? = null,
    val availableColors: List<String> = emptyList(),
    val availableSizes: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
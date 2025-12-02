package org.christophertwo.cotizador.feature.quote.presentation

import org.christophertwo.cotizador.feature.quote.domain.SaleType

sealed interface QuoteAction {
    data class SelectSaleType(val saleType: SaleType) : QuoteAction
    data class SelectColor(val color: String) : QuoteAction
    data class SelectSize(val size: String) : QuoteAction
    data object AddPrint : QuoteAction
    data class RemovePrint(val position: Int) : QuoteAction
    data class UpdatePrintPrice(val position: Int, val price: Double) : QuoteAction
    data class UpdateQuantity(val delta: Int) : QuoteAction
    data class UpdateProfitMargin(val margin: Float) : QuoteAction
    data object NavigateBack : QuoteAction
}
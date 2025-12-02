package org.christophertwo.cotizador.feature.quote.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.christophertwo.cotizador.core.util.formatDecimals
import org.christophertwo.cotizador.data.YazbekService
import org.christophertwo.cotizador.feature.quote.domain.PriceBreakdown
import org.christophertwo.cotizador.feature.quote.domain.PrintConfig
import org.christophertwo.cotizador.feature.quote.domain.SaleType

class QuoteViewModel(
    private val productCode: String,
    private val onNavigateBack: () -> Unit
) : ViewModel() {

    private val yazbekService = YazbekService()

    private val _state = MutableStateFlow(QuoteState())
    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = QuoteState()
        )

    init {
        loadProduct(productCode)
    }

    private fun loadProduct(code: String) {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true, error = null) }

                val yazbekData = yazbekService.loadFromResources()
                val product = yazbekData.catalogo.flatMap { it.productos }
                    .find { it.codigo == code }

                if (product != null) {
                    val colors = product.precios.map { it.color }.distinct()
                    val sizes = product.precios.map { it.talla }.distinct()

                    _state.update {
                        it.copy(
                            product = product,
                            availableColors = colors,
                            availableSizes = sizes,
                            isLoading = false
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "Producto no encontrado"
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar el producto: ${e.message}"
                    )
                }
            }
        }
    }

    fun onAction(action: QuoteAction) {
        when (action) {
            is QuoteAction.SelectSaleType -> {
                _state.update { it.copy(saleType = action.saleType) }
                recalculatePrice()
            }

            is QuoteAction.SelectColor -> {
                _state.update { it.copy(selectedColor = action.color) }
                updateAvailableSizes()
                recalculatePrice()
            }

            is QuoteAction.SelectSize -> {
                _state.update { it.copy(selectedSize = action.size) }
                recalculatePrice()
            }

            QuoteAction.AddPrint -> {
                val currentConfigs = _state.value.printConfigs
                if (currentConfigs.size < 4) {
                    val newConfig = PrintConfig(
                        position = currentConfigs.size + 1,
                        price = 0.0
                    )
                    _state.update {
                        it.copy(printConfigs = currentConfigs + newConfig)
                    }
                    recalculatePrice()
                }
            }

            is QuoteAction.RemovePrint -> {
                val currentConfigs = _state.value.printConfigs
                _state.update {
                    it.copy(
                        printConfigs = currentConfigs
                            .filter { config -> config.position != action.position }
                            .mapIndexed { index, config ->
                                config.copy(position = index + 1)
                            }
                    )
                }
                recalculatePrice()
            }

            is QuoteAction.UpdatePrintPrice -> {
                val updatedConfigs = _state.value.printConfigs.map { config ->
                    if (config.position == action.position) {
                        config.copy(price = action.price)
                    } else {
                        config
                    }
                }
                _state.update { it.copy(printConfigs = updatedConfigs) }
                recalculatePrice()
            }

            is QuoteAction.UpdateQuantity -> {
                val newQuantity = (_state.value.quantity + action.delta).coerceAtLeast(1)
                _state.update { it.copy(quantity = newQuantity) }
                recalculatePrice()
            }

            is QuoteAction.UpdateProfitMargin -> {
                val margin = action.margin.coerceIn(0f, 100f)
                _state.update { it.copy(profitMargin = margin) }
                recalculatePrice()
            }

            QuoteAction.NavigateBack -> {
                onNavigateBack()
            }
        }
    }

    private fun updateAvailableSizes() {
        val currentState = _state.value
        val product = currentState.product ?: return
        val selectedColor = currentState.selectedColor ?: return

        val sizesForColor = product.precios
            .filter { it.color == selectedColor }
            .map { it.talla }
            .distinct()

        _state.update {
            it.copy(
                availableSizes = sizesForColor,
                // Reset size selection if not available for new color
                selectedSize = if (it.selectedSize in sizesForColor) it.selectedSize else null
            )
        }
    }

    private fun recalculatePrice() {
        val currentState = _state.value
        val product = currentState.product ?: return
        val color = currentState.selectedColor ?: return
        val size = currentState.selectedSize ?: return

        // Find the price variant
        val priceVariant = product.precios.find {
            it.color == color && it.talla == size
        } ?: return

        // Get base price according to sale type
        val basePrice = when (currentState.saleType) {
            SaleType.MAYOREO -> priceVariant.precioMayoreo
            SaleType.MENUDEO -> priceVariant.precioMenudeo
        }

        val basePriceExplanation = buildString {
            append("Precio base (${currentState.saleType.name.lowercase().replaceFirstChar { it.uppercase() }})")
            append(" - $color - $size: $${basePrice.formatDecimals()}")
        }

        // Calculate prints cost
        val totalPrintsCost = currentState.printConfigs.sumOf { it.price }
        val printsExplanation = if (currentState.printConfigs.isNotEmpty()) {
            buildString {
                append("Impresiones: ")
                append(currentState.printConfigs.joinToString(" + ") {
                    "$${it.price.formatDecimals()}"
                })
                append(" = $${totalPrintsCost.formatDecimals()}")
            }
        } else {
            "Sin impresiones"
        }

        // Subtotal without profit
        val subtotalWithoutProfit = basePrice + totalPrintsCost

        // Calculate profit
        val profitAmount = subtotalWithoutProfit * (currentState.profitMargin / 100f)
        val profitExplanation =
            "${currentState.profitMargin.formatDecimals(1)}% de ganancia: $${profitAmount.formatDecimals()}"

        // Final price per unit
        val finalPricePerUnit = subtotalWithoutProfit + profitAmount

        // Total
        val totalFinal = finalPricePerUnit * currentState.quantity

        val breakdown = PriceBreakdown(
            basePrice = basePrice,
            basePriceExplanation = basePriceExplanation,
            totalPrintsCost = totalPrintsCost,
            printsExplanation = printsExplanation,
            subtotalWithoutProfit = subtotalWithoutProfit,
            profitAmount = profitAmount,
            profitExplanation = profitExplanation,
            finalPricePerUnit = finalPricePerUnit,
            quantity = currentState.quantity,
            totalFinal = totalFinal
        )

        _state.update { it.copy(priceBreakdown = breakdown) }
    }
}
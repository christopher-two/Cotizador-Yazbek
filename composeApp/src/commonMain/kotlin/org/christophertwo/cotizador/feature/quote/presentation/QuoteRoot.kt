package org.christophertwo.cotizador.feature.quote.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.christophertwo.cotizador.core.ui.AppTheme
import org.christophertwo.cotizador.feature.quote.presentation.components.*
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun QuoteRoot(
    viewModel: QuoteViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    QuoteScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteScreen(
    state: QuoteState,
    onAction: (QuoteAction) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.product?.descripcion ?: "Cotización",
                        maxLines = 2
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(QuoteAction.NavigateBack) }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            state.product != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SaleTypeSelector(
                        selectedType = state.saleType,
                        onTypeSelected = { onAction(QuoteAction.SelectSaleType(it)) }
                    )

                    ColorButtonGroup(
                        availableColors = state.availableColors,
                        selectedColor = state.selectedColor,
                        onColorSelected = { onAction(QuoteAction.SelectColor(it)) }
                    )

                    SizeButtonGroup(
                        availableSizes = state.availableSizes,
                        selectedSize = state.selectedSize,
                        onSizeSelected = { onAction(QuoteAction.SelectSize(it)) },
                        enabled = state.selectedColor != null
                    )

                    PrintConfigSection(
                        printConfigs = state.printConfigs,
                        onAddPrint = { onAction(QuoteAction.AddPrint) },
                        onRemovePrint = { position ->
                            onAction(QuoteAction.RemovePrint(position))
                        },
                        onUpdatePrice = { position, price ->
                            onAction(QuoteAction.UpdatePrintPrice(position, price))
                        }
                    )

                    QuantitySelector(
                        quantity = state.quantity,
                        onQuantityChange = { delta ->
                            onAction(QuoteAction.UpdateQuantity(delta))
                        }
                    )

                    ProfitMarginSlider(
                        profitMargin = state.profitMargin,
                        onProfitMarginChange = { margin ->
                            onAction(QuoteAction.UpdateProfitMargin(margin))
                        }
                    )

                    PriceSummaryCard(
                        breakdown = state.priceBreakdown
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        QuoteScreen(
            state = QuoteState(),
            onAction = {}
        )
    }
}
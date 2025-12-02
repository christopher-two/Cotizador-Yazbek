package org.christophertwo.cotizador.feature.quote.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.christophertwo.cotizador.core.ui.AppTheme
import org.christophertwo.cotizador.feature.quote.domain.SaleType
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Product header
                    item {
                        ProductHeader(
                            code = state.product.codigo,
                            description = state.product.descripcion
                        )
                    }

                    // Sale type selector
                    item {
                        SaleTypeSelector(
                            selectedType = state.saleType,
                            onTypeSelected = { onAction(QuoteAction.SelectSaleType(it)) }
                        )
                    }

                    // Color selector
                    item {
                        ColorChipGroup(
                            availableColors = state.availableColors,
                            selectedColor = state.selectedColor,
                            onColorSelected = { onAction(QuoteAction.SelectColor(it)) }
                        )
                    }

                    // Size selector
                    item {
                        SizeChipGroup(
                            availableSizes = state.availableSizes,
                            selectedSize = state.selectedSize,
                            onSizeSelected = { onAction(QuoteAction.SelectSize(it)) },
                            enabled = state.selectedColor != null
                        )
                    }

                    // Print configuration
                    item {
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
                    }

                    // Quantity selector
                    item {
                        QuantitySelector(
                            quantity = state.quantity,
                            onQuantityChange = { delta ->
                                onAction(QuoteAction.UpdateQuantity(delta))
                            }
                        )
                    }

                    // Profit margin slider
                    item {
                        ProfitMarginSlider(
                            profitMargin = state.profitMargin,
                            onProfitMarginChange = { margin ->
                                onAction(QuoteAction.UpdateProfitMargin(margin))
                            }
                        )
                    }

                    // Price summary
                    item {
                        PriceSummaryCard(
                            breakdown = state.priceBreakdown
                        )
                    }

                    // Bottom spacer
                    item {
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductHeader(
    code: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Código: $code",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = description,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
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
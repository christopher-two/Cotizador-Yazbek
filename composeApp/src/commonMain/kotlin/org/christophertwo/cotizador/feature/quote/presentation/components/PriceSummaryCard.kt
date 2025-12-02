package org.christophertwo.cotizador.feature.quote.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.christophertwo.cotizador.core.util.formatDecimals
import org.christophertwo.cotizador.feature.quote.domain.PriceBreakdown

@Composable
fun PriceSummaryCard(
    breakdown: PriceBreakdown?,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(true) }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Resumen de Cotización",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "Contraer" else "Expandir"
                    )
                }
            }

            if (breakdown == null) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Selecciona tipo de venta, color y talla para ver el resumen",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            } else {
                AnimatedVisibility(visible = isExpanded) {
                    Column(
                        modifier = Modifier.padding(top = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Divider()

                        // Base price
                        PriceDetailRow(
                            label = "Precio Base",
                            value = "$${breakdown.basePrice.formatDecimals()}",
                            explanation = breakdown.basePriceExplanation
                        )

                        // Prints cost
                        if (breakdown.totalPrintsCost > 0) {
                            PriceDetailRow(
                                label = "Impresiones",
                                value = "$${breakdown.totalPrintsCost.formatDecimals()}",
                                explanation = breakdown.printsExplanation
                            )
                        }

                        Divider()

                        // Subtotal without profit
                        PriceDetailRow(
                            label = "Subtotal sin Ganancia",
                            value = "$${breakdown.subtotalWithoutProfit.formatDecimals()}",
                            isBold = true
                        )

                        // Profit margin
                        if (breakdown.profitAmount > 0) {
                            PriceDetailRow(
                                label = "Ganancia",
                                value = "$${breakdown.profitAmount.formatDecimals()}",
                                explanation = breakdown.profitExplanation,
                                valueColor = MaterialTheme.colorScheme.tertiary
                            )
                        }

                        Divider()

                        // Final price per unit
                        PriceDetailRow(
                            label = "Precio por Unidad",
                            value = "$${breakdown.finalPricePerUnit.formatDecimals()}",
                            isBold = true
                        )

                        // Quantity
                        PriceDetailRow(
                            label = "Cantidad",
                            value = "${breakdown.quantity} piezas"
                        )

                        HorizontalDivider(
                            thickness = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        // Total final
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "TOTAL",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "$${breakdown.totalFinal.formatDecimals()}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PriceDetailRow(
    label: String,
    value: String,
    explanation: String? = null,
    isBold: Boolean = false,
    valueColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onPrimaryContainer,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = label,
                style = if (isBold)
                    MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                else
                    MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = value,
                style = if (isBold)
                    MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                else
                    MaterialTheme.typography.bodyLarge,
                color = valueColor
            )
        }

        if (explanation != null) {
            Text(
                text = explanation,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}


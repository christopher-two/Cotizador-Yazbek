package org.christophertwo.cotizador.feature.quote.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.christophertwo.cotizador.feature.quote.domain.PrintConfig

@Composable
fun PrintConfigSection(
    printConfigs: List<PrintConfig>,
    onAddPrint: () -> Unit,
    onRemovePrint: (Int) -> Unit,
    onUpdatePrice: (Int, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Impresiones (0-4)",
                style = MaterialTheme.typography.titleMedium
            )

            FilledTonalButton(
                onClick = onAddPrint,
                enabled = printConfigs.size < 4
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Agregar impresión",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text("Agregar")
            }
        }

        if (printConfigs.isEmpty()) {
            Text(
                text = "Sin impresiones agregadas",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        } else {
            Spacer(Modifier.height(8.dp))

            printConfigs.forEach { config ->
                PrintConfigItem(
                    config = config,
                    onRemove = { onRemovePrint(config.position) },
                    onPriceChange = { newPrice -> onUpdatePrice(config.position, newPrice) },
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun PrintConfigItem(
    config: PrintConfig,
    onRemove: () -> Unit,
    onPriceChange: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    var priceText by remember(config.price) {
        mutableStateOf(if (config.price == 0.0) "" else config.price.toString())
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = "Impresión ${config.position}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.width(100.dp)
            )

            OutlinedTextField(
                value = priceText,
                onValueChange = { newValue ->
                    priceText = newValue
                    val price = newValue.toDoubleOrNull() ?: 0.0
                    onPriceChange(price)
                },
                label = { Text("Precio") },
                prefix = { Text("$") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = onRemove) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar impresión",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}


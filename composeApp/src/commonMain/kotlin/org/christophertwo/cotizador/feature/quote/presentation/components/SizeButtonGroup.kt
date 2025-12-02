package org.christophertwo.cotizador.feature.quote.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.christophertwo.cotizador.core.common.SizeContent

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SizeButtonGroup(
    availableSizes: List<String>,
    selectedSize: String?,
    onSizeSelected: (String) -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.widthIn(
            min = SizeContent.MIN.width.dp,
            max = SizeContent.MAX.width.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Talla del Producto",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (!enabled) {
                Text(
                    text = "Selecciona un color primero",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                availableSizes.forEach { size ->
                    SegmentedButton(
                        selected = selectedSize == size,
                        onClick = { onSizeSelected(size) },
                        label = { Text(size) },
                        enabled = enabled,
                        shape = SegmentedButtonDefaults.itemShape(
                            index = availableSizes.indexOf(size),
                            count = availableSizes.size
                        ),
                    )
                }
            }
        }
    }
}

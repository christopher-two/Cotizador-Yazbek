package org.christophertwo.cotizador.feature.quote.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.christophertwo.cotizador.feature.quote.domain.SaleType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleTypeSelector(
    selectedType: SaleType,
    onTypeSelected: (SaleType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Tipo de Venta",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            SegmentedButton(
                selected = selectedType == SaleType.MAYOREO,
                onClick = { onTypeSelected(SaleType.MAYOREO) },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
            ) {
                Text("Mayoreo")
            }
            SegmentedButton(
                selected = selectedType == SaleType.MENUDEO,
                onClick = { onTypeSelected(SaleType.MENUDEO) },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
            ) {
                Text("Menudeo")
            }
        }
    }
}


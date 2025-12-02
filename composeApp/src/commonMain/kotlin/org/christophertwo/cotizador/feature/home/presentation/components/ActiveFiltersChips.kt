package org.christophertwo.cotizador.feature.home.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.christophertwo.cotizador.core.util.formatDecimals

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ActiveFiltersChips(
    selectedCategory: String?,
    minPrice: Double?,
    maxPrice: Double?,
    selectedNeckType: String?,
    selectedSleeveType: String?,
    selectedGender: String?,
    selectedCharacteristic: String?,
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Chip de categoría
        if (selectedCategory != null) {
            InputChip(
                selected = true,
                onClick = onClearFilters,
                label = { Text(selectedCategory) },
                trailingIcon = {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Quitar filtro",
                        modifier = Modifier.size(18.dp)
                    )
                }
            )
        }

        // Chip de tipo de cuello
        if (selectedNeckType != null) {
            InputChip(
                selected = true,
                onClick = onClearFilters,
                label = { Text("Cuello: $selectedNeckType") },
                trailingIcon = {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Quitar filtro",
                        modifier = Modifier.size(18.dp)
                    )
                }
            )
        }

        // Chip de tipo de manga
        if (selectedSleeveType != null) {
            InputChip(
                selected = true,
                onClick = onClearFilters,
                label = { Text("Manga: $selectedSleeveType") },
                trailingIcon = {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Quitar filtro",
                        modifier = Modifier.size(18.dp)
                    )
                }
            )
        }

        // Chip de género
        if (selectedGender != null) {
            InputChip(
                selected = true,
                onClick = onClearFilters,
                label = { Text(selectedGender) },
                trailingIcon = {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Quitar filtro",
                        modifier = Modifier.size(18.dp)
                    )
                }
            )
        }

        // Chip de característica
        if (selectedCharacteristic != null) {
            InputChip(
                selected = true,
                onClick = onClearFilters,
                label = { Text(selectedCharacteristic) },
                trailingIcon = {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Quitar filtro",
                        modifier = Modifier.size(18.dp)
                    )
                }
            )
        }

        // Chip de rango de precio
        if (minPrice != null || maxPrice != null) {
            val priceText = when {
                minPrice != null && maxPrice != null ->
                    "$${minPrice.formatDecimals()} - $${maxPrice.formatDecimals()}"
                minPrice != null ->
                    "Desde $${minPrice.formatDecimals()}"
                else ->
                    "Hasta $${maxPrice!!.formatDecimals()}"
            }

            InputChip(
                selected = true,
                onClick = onClearFilters,
                label = { Text(priceText) },
                trailingIcon = {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Quitar filtro",
                        modifier = Modifier.size(18.dp)
                    )
                }
            )
        }

        // Botón limpiar todo si hay filtros
        if (selectedCategory != null ||
            minPrice != null ||
            maxPrice != null ||
            selectedNeckType != null ||
            selectedSleeveType != null ||
            selectedGender != null ||
            selectedCharacteristic != null) {
            TextButton(
                onClick = onClearFilters,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    "Limpiar todo",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}


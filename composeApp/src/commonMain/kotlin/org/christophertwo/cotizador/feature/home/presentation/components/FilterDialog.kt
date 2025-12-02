package org.christophertwo.cotizador.feature.home.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterDialog(
    isOpen: Boolean,
    availableCategories: List<String>,
    selectedCategory: String?,
    minPrice: Double?,
    maxPrice: Double?,
    // Atributos dinámicos
    availableNeckTypes: List<String>,
    selectedNeckType: String?,
    availableSleeveTypes: List<String>,
    selectedSleeveType: String?,
    availableGenders: List<String>,
    selectedGender: String?,
    availableCharacteristics: List<String>,
    selectedCharacteristic: String?,
    // Callbacks
    onDismiss: () -> Unit,
    onCategorySelected: (String?) -> Unit,
    onPriceRangeChanged: (Double?, Double?) -> Unit,
    onNeckTypeSelected: (String?) -> Unit,
    onSleeveTypeSelected: (String?) -> Unit,
    onGenderSelected: (String?) -> Unit,
    onCharacteristicSelected: (String?) -> Unit,
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isOpen) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            modifier = modifier
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 32.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Filtros",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    TextButton(onClick = onClearFilters) {
                        Text("Limpiar todo")
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Categorías
                Text(
                    text = "Categoría",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Chip "Todas"
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { onCategorySelected(null) },
                        label = { Text("Todas") }
                    )

                    // Chips de categorías
                    availableCategories.forEach { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { onCategorySelected(category) },
                            label = { Text(category) }
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Filtros dinámicos según la categoría
                // Solo mostrar si hay opciones disponibles

                // Tipo de cuello
                if (availableNeckTypes.isNotEmpty()) {
                    Text(
                        text = "Tipo de Cuello",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        FilterChip(
                            selected = selectedNeckType == null,
                            onClick = { onNeckTypeSelected(null) },
                            label = { Text("Todos") }
                        )

                        availableNeckTypes.forEach { neckType ->
                            FilterChip(
                                selected = selectedNeckType == neckType,
                                onClick = { onNeckTypeSelected(neckType) },
                                label = { Text(neckType) }
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))
                }

                // Tipo de manga
                if (availableSleeveTypes.isNotEmpty()) {
                    Text(
                        text = "Tipo de Manga",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        FilterChip(
                            selected = selectedSleeveType == null,
                            onClick = { onSleeveTypeSelected(null) },
                            label = { Text("Todas") }
                        )

                        availableSleeveTypes.forEach { sleeveType ->
                            FilterChip(
                                selected = selectedSleeveType == sleeveType,
                                onClick = { onSleeveTypeSelected(sleeveType) },
                                label = { Text(sleeveType) }
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))
                }

                // Género
                if (availableGenders.isNotEmpty()) {
                    Text(
                        text = "Género",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        FilterChip(
                            selected = selectedGender == null,
                            onClick = { onGenderSelected(null) },
                            label = { Text("Todos") }
                        )

                        availableGenders.forEach { gender ->
                            FilterChip(
                                selected = selectedGender == gender,
                                onClick = { onGenderSelected(gender) },
                                label = { Text(gender) }
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))
                }

                // Características especiales
                if (availableCharacteristics.isNotEmpty()) {
                    Text(
                        text = "Características",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        FilterChip(
                            selected = selectedCharacteristic == null,
                            onClick = { onCharacteristicSelected(null) },
                            label = { Text("Todas") }
                        )

                        availableCharacteristics.forEach { characteristic ->
                            FilterChip(
                                selected = selectedCharacteristic == characteristic,
                                onClick = { onCharacteristicSelected(characteristic) },
                                label = { Text(characteristic) }
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))
                }

                // Rango de precio
                Text(
                    text = "Rango de Precio",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                PriceRangeSelector(
                    minPrice = minPrice,
                    maxPrice = maxPrice,
                    onPriceRangeChanged = onPriceRangeChanged
                )

                Spacer(Modifier.height(24.dp))

                // Botón aplicar
                FilledTonalButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Aplicar filtros")
                }
            }
        }
    }
}

@Composable
private fun PriceRangeSelector(
    minPrice: Double?,
    maxPrice: Double?,
    onPriceRangeChanged: (Double?, Double?) -> Unit,
    modifier: Modifier = Modifier
) {
    var minPriceText by remember(minPrice) {
        mutableStateOf(minPrice?.toString() ?: "")
    }
    var maxPriceText by remember(maxPrice) {
        mutableStateOf(maxPrice?.toString() ?: "")
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = minPriceText,
            onValueChange = {
                minPriceText = it
                val min = it.toDoubleOrNull()
                onPriceRangeChanged(min, maxPrice)
            },
            label = { Text("Desde") },
            prefix = { Text("$") },
            singleLine = true,
            modifier = Modifier.weight(1f)
        )

        OutlinedTextField(
            value = maxPriceText,
            onValueChange = {
                maxPriceText = it
                val max = it.toDoubleOrNull()
                onPriceRangeChanged(minPrice, max)
            },
            label = { Text("Hasta") },
            prefix = { Text("$") },
            singleLine = true,
            modifier = Modifier.weight(1f)
        )
    }
}


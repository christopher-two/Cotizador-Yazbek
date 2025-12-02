package org.christophertwo.cotizador.feature.home.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.christophertwo.cotizador.core.common.RouteScreen
import org.christophertwo.cotizador.data.Product
import org.christophertwo.cotizador.feature.home.presentation.components.ActiveFiltersChips
import org.christophertwo.cotizador.feature.home.presentation.components.FilterDialog
import org.christophertwo.cotizador.feature.home.presentation.components.ProductCard
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HomeRoot(
    viewModel: HomeViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavigateToQuote = { productCode ->
            navController.navigate(RouteScreen.Quote(productCode))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    onNavigateToQuote: (String) -> Unit = {}
) {
    Scaffold(
        topBar = {
            Column {
                HomeSearchBar(
                    state = state,
                    onAction = onAction,
                    onNavigateToQuote = onNavigateToQuote
                )

                // Mostrar chips de filtros activos
                if (state.selectedCategory != null || state.minPriceFilter != null || state.maxPriceFilter != null) {
                    ActiveFiltersChips(
                        selectedCategory = state.selectedCategory,
                        minPrice = state.minPriceFilter,
                        maxPrice = state.maxPriceFilter,
                        onClearFilters = { onAction(HomeAction.ClearFilters) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        selectedNeckType = state.selectedNeckType,
                        selectedSleeveType = state.selectedSleeveType,
                        selectedGender = state.selectedGender,
                        selectedCharacteristic = state.selectedCharacteristic
                    )
                }
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    state.error != null -> {
                        Text(
                            text = state.error,
                            color = colorScheme.error,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp)
                        )
                    }

                    state.displayedProducts.isEmpty() && !state.isSearchActive -> {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (state.searchQuery.isNotEmpty()) {
                                    "No se encontraron productos"
                                } else {
                                    "No hay productos disponibles"
                                },
                                style = MaterialTheme.typography.bodyLarge,
                                color = colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    else -> {
                        ProductsGrid(
                            products = state.displayedProducts,
                            hasMoreProducts = state.hasMoreProducts,
                            onProductClick = onNavigateToQuote,
                            onLoadMore = { onAction(HomeAction.LoadMoreProducts) }
                        )
                    }
                }
            }
        }
    )

    // Diálogo de filtros
    FilterDialog(
        isOpen = state.isFilterDialogOpen,
        availableCategories = state.availableCategories,
        selectedCategory = state.selectedCategory,
        minPrice = state.minPriceFilter,
        maxPrice = state.maxPriceFilter,
        availableNeckTypes = state.availableNeckTypes,
        selectedNeckType = state.selectedNeckType,
        availableSleeveTypes = state.availableSleeveTypes,
        selectedSleeveType = state.selectedSleeveType,
        availableGenders = state.availableGenders,
        selectedGender = state.selectedGender,
        availableCharacteristics = state.availableCharacteristics,
        selectedCharacteristic = state.selectedCharacteristic,
        onDismiss = { onAction(HomeAction.ToggleFilterDialog) },
        onCategorySelected = { onAction(HomeAction.SelectCategory(it)) },
        onPriceRangeChanged = { min, max -> onAction(HomeAction.SetPriceRange(min, max)) },
        onNeckTypeSelected = { onAction(HomeAction.SelectNeckType(it)) },
        onSleeveTypeSelected = { onAction(HomeAction.SelectSleeveType(it)) },
        onGenderSelected = { onAction(HomeAction.SelectGender(it)) },
        onCharacteristicSelected = { onAction(HomeAction.SelectCharacteristic(it)) },
        onClearFilters = { onAction(HomeAction.ClearFilters) }
    )
}

@Composable
private fun ProductsGrid(
    products: List<Product>,
    hasMoreProducts: Boolean,
    onProductClick: (String) -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 220.dp), // Tamaño adaptativo para mejor visualización
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp), // Más padding para respiración
        horizontalArrangement = Arrangement.spacedBy(16.dp), // Más espacio horizontal
        verticalArrangement = Arrangement.spacedBy(16.dp) // Más espacio vertical
    ) {
        items(products) { product ->
            ProductCard(
                product = product,
                onClick = { onProductClick(product.codigo) }
            )
        }

        // Botón de cargar más al final si hay más productos
        if (hasMoreProducts) {
            item {
                FilledTonalButton(
                    onClick = onLoadMore,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        "Cargar más productos",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSearchBar(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    onNavigateToQuote: (String) -> Unit = {}
) {
    val searchBarState = rememberSearchBarState()

    // Calcular si hay filtros activos
    val hasActiveFilters = state.selectedCategory != null ||
                          state.minPriceFilter != null ||
                          state.maxPriceFilter != null ||
                          state.selectedNeckType != null ||
                          state.selectedSleeveType != null ||
                          state.selectedGender != null ||
                          state.selectedCharacteristic != null

    TopSearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = state.searchQuery,
                onQueryChange = { onAction(HomeAction.UpdateSearchQuery(it)) },
                onSearch = { onAction(HomeAction.SetSearchActive(false)) },
                expanded = state.isSearchActive,
                onExpandedChange = { onAction(HomeAction.SetSearchActive(it)) },
                placeholder = { Text("Buscar productos...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    BadgedBox(
                        badge = {
                            if (hasActiveFilters) {
                                Badge()
                            }
                        }
                    ) {
                        IconButton(onClick = { onAction(HomeAction.ToggleFilterDialog) }) {
                            Icon(
                                Icons.Default.Tune,
                                contentDescription = "Filtros",
                                tint = if (hasActiveFilters)
                                    colorScheme.primary
                                else
                                    colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = MaterialTheme.shapes.extraLarge,
        colors = SearchBarDefaults.colors(),
        tonalElevation = 8.dp,
        shadowElevation = 4.dp,
        state = searchBarState,
        windowInsets = WindowInsets.statusBars,
    )
}

@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        HomeScreen(
            state = HomeState(),
            onAction = {}
        )
    }
}
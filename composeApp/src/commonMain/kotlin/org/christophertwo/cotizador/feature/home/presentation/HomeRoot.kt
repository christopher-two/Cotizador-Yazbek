package org.christophertwo.cotizador.feature.home.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.christophertwo.cotizador.core.common.RouteScreen
import org.christophertwo.cotizador.data.Product
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
            HomeSearchBar(
                state = state,
                onAction = onAction,
                onNavigateToQuote = onNavigateToQuote
            )
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
                            color = MaterialTheme.colorScheme.error,
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
                                text = "Busca un producto para comenzar",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
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
        columns = GridCells.Adaptive(minSize = 160.dp),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = onLoadMore,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cargar más productos")
                    }
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
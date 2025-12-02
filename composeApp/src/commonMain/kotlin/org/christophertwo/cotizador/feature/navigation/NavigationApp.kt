package org.christophertwo.cotizador.feature.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.christophertwo.cotizador.core.common.RouteScreen
import org.christophertwo.cotizador.feature.home.presentation.HomeRoot
import org.christophertwo.cotizador.feature.home.presentation.HomeViewModel
import org.christophertwo.cotizador.feature.quote.presentation.QuoteRoot
import org.christophertwo.cotizador.feature.quote.presentation.QuoteViewModel

@Composable
fun NavigationApp(
    navController: NavHostController,
    startDestination: RouteScreen,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<RouteScreen.Home> {
            HomeRoot(
                viewModel = viewModel { HomeViewModel() },
                navController = navController
            )
        }
        composable<RouteScreen.Quote> { backStackEntry ->
            val route = backStackEntry.toRoute<RouteScreen.Quote>()
            QuoteRoot(
                viewModel = viewModel {
                    QuoteViewModel(
                        productCode = route.productCode,
                        onNavigateBack = { navController.navigateUp() }
                    )
                }
            )
        }
    }
}
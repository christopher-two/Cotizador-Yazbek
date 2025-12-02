package org.christophertwo.cotizador

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import org.christophertwo.cotizador.core.common.RouteScreen
import org.christophertwo.cotizador.core.ui.AppTheme
import org.christophertwo.cotizador.feature.navigation.NavigationApp

@Composable
fun App() {
    AppTheme {
        NavigationApp(
            navController = rememberNavController(),
            startDestination = RouteScreen.Home
        )
    }
}
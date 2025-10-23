package com.example.smartfarm.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Routes(
    val route: String,
    val hasBottomBar: Boolean = false,
) {
    object Login : Routes("login", false)
    object Home : Routes("home", true)
    object Dashboard : Routes("dashboard", true)
    object Expenses : Routes("expenses", true)
    object Weather : Routes("weather", true)
    object Settings : Routes("settings", true)
}

///////////////////////////////////////////////////////////////////////////
// BottomNavs navigation
///////////////////////////////////////////////////////////////////////////
sealed class BottomRoutes(
    val title: String,
    val icon: ImageVector,
    val route: String
){
    object Home : BottomRoutes("Dashboard", Icons.Default.Home, Routes.Home.route)
    object Weather : BottomRoutes("Weather", Icons.Default.WbSunny, Routes.Weather.route)
    object Expenses : BottomRoutes("Expenses", Icons.Default.AccountBalanceWallet, Routes.Expenses.route)

    object Settings : BottomRoutes("Settings", Icons.Default.Settings, Routes.Settings.route)

    companion object{
        val bottomItem = listOf(
            Home,
            Weather,
            Expenses,
            Settings
        )
    }
}

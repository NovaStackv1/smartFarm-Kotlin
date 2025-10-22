package com.example.smartfarm.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
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
}

///////////////////////////////////////////////////////////////////////////
// BottomNavs navigation
///////////////////////////////////////////////////////////////////////////
sealed class BottomRoutes(
    val title: String,
    val icon: ImageVector,
    val route: String
){
    object Home : BottomRoutes("Home", Icons.Default.Home, Routes.Home.route)
    object Weather : BottomRoutes("Dashboard", Icons.Default.Home, Routes.Weather.route)
    object Expenses : BottomRoutes("Expenses", Icons.Default.Home, Routes.Expenses.route)
}

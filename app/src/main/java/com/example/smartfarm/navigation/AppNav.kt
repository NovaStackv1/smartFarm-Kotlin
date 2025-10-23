package com.example.smartfarm.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.smartfarm.ui.features.auth.view.LoginScreen
import com.example.smartfarm.ui.features.finance.presentation.view.FinanceScreen
import com.example.smartfarm.ui.features.home.presentation.view.HomeScreen
import com.example.smartfarm.ui.features.settings.presentation.view.SettingScreen
import com.example.smartfarm.ui.features.weather.presentation.view.WeatherScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    val startRoute = Routes.Login.route


    var showBottomBar by remember { mutableStateOf(true) }

    Scaffold (
        containerColor = MaterialTheme.colorScheme.background,

        bottomBar = {
            // only show bottom bar for bottom-tab routes AND when showBottomBar == true
            val shouldShow = showBottomBar && (currentRoute in BottomRoutes.bottomItem.map { it.route })

            AnimatedVisibility(
                visible = shouldShow,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                BottomBarNav(
                    navController = navController
                )
            }
        }
    ){ paddingValues ->

        NavHost(
            navController = navController,
            startDestination = startRoute,
            modifier = modifier.padding(paddingValues)
        ) {
            composable (Routes.Login.route){
                LoginScreen(
                    onNavigateToHome = {
                        navController.navigate(Routes.Home.route){
                            popUpTo(Routes.Login.route){
                                inclusive = true
                            }
                        }
                    }
                )

            }

            composable(Routes.Home.route){
                HomeScreen()
            }

            composable(Routes.Weather.route){
                WeatherScreen(
                    onNavigateBack = {
                        navController.navigateUp()
                    }
                )

            }

            composable (Routes.Expenses.route){
                FinanceScreen()

            }

            composable (Routes.Settings.route){
                SettingScreen()

            }

        }
    }

}
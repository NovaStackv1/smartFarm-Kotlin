package com.example.smartfarm.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smartfarm.ui.features.auth.view.LoginScreen
import com.example.smartfarm.ui.features.finance.presentation.view.FinanceScreen
import com.example.smartfarm.ui.features.home.presentation.HomeScreen
import com.example.smartfarm.ui.features.settings.presentation.view.SettingScreen
import com.example.smartfarm.ui.features.weather.presentation.view.WeatherScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    val startRoute = Routes.Home.route


    Scaffold (
        containerColor = MaterialTheme.colorScheme.background,

        bottomBar = {
            BottomBarNav(
                navController = navController
            )
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
                        navController.navigate(Routes.Home.route)
                    }
                )

            }

            composable(Routes.Home.route){
                HomeScreen(
                    onNavigate = { route -> navController.navigate(route) },
                )
            }

            composable(Routes.Weather.route){
                WeatherScreen()

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
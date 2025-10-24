package com.example.smartfarm.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.smartfarm.ui.features.auth.view.LoginScreen
import com.example.smartfarm.ui.features.finance.presentation.view.FinanceScreen
import com.example.smartfarm.ui.features.home.presentation.view.HomeScreen
import com.example.smartfarm.ui.features.settings.presentation.view.SettingsScreen
import com.example.smartfarm.ui.features.weather.presentation.view.WeatherScreen
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    val startRoute = Routes.Login.route


    var showBottomBar by remember { mutableStateOf(true) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val showComingSoonSnackbar: () -> Unit = {
        scope.launch {
            snackbarHostState.showSnackbar(
                message = "Feature coming soon! ✨",
                actionLabel = "Dismiss" // Optional action label
            )
        }
    }

    Scaffold (
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },

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
                SettingsScreen(

                    onNavigateBack = {
                        navController.navigateUp()
                    },
                    onNavigateToProfile = showComingSoonSnackbar,
                    onNavigateToFarmPreferences = showComingSoonSnackbar,
                    onNavigateToAccountSettings = showComingSoonSnackbar,
                    onNavigateToHelpSupport = showComingSoonSnackbar,
                    onNavigateToNotifications = showComingSoonSnackbar,
                    onNavigateToAbout = showComingSoonSnackbar,
                )

            }

        }
    }

}
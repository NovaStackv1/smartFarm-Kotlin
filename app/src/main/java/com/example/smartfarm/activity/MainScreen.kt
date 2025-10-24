package com.example.smartfarm.activity

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.smartfarm.navigation.NavGraph
import com.example.smartfarm.navigation.Routes
import com.example.smartfarm.ui.features.auth.domain.model.AuthState
import com.example.smartfarm.ui.features.auth.viewModel.LoginViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val authState by loginViewModel.authState.collectAsState()
    
    // Handle initial navigation based on auth state
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                // If already authenticated, navigate to Home and clear back stack
                navController.navigate(Routes.Home.route) {
                    popUpTo(0) { // Clear entire back stack
                        inclusive = true
                    }
                }
            }
            is AuthState.Idle -> {
                // Stay on login screen for new users
                if (navController.currentBackStackEntry?.destination?.route != Routes.Login.route) {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
            else -> {
                // For Loading or Error states, stay on current screen
            }
        }
    }

    // existing NavGraph
    //NavGraph(navController = navController)
}
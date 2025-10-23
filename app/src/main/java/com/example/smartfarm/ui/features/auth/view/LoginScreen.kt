package com.example.smartfarm.ui.features.auth.view

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartfarm.ui.features.auth.components.LoginContent
import com.example.smartfarm.ui.features.auth.viewModel.LoginViewModel
import com.example.smartfarm.ui.features.auth.viewModel.NavigationEvent

@SuppressLint("ContextCastToActivity")
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val authState by loginViewModel.authState.collectAsState()

    // Get the activity context safely
    val activity = LocalContext.current as? ComponentActivity

    // Handle navigation events
    LaunchedEffect(Unit) {
        loginViewModel.navigationEvent.collect { event ->
            when (event) {
                is NavigationEvent.NavigateToDashboard -> onNavigateToHome()
            }
        }
    }


    LoginContent(
        authState = authState,
        onGoogleSignInClick = {
            // Only proceed if we have an Activity context
            if (activity != null) {
                loginViewModel.signInWithGoogle(activity)
            } else {
                // Fallback: show error or use application context (may still fail)
                loginViewModel.signInWithGoogle(context)
            }
        },
        onDismissError = { loginViewModel.clearError() }
    )
}
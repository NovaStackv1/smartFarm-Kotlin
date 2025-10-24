package com.example.smartfarm.activity

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.smartfarm.navigation.NavGraph
import com.example.smartfarm.ui.features.auth.view.LoginScreen
import com.example.smartfarm.ui.features.settings.presentation.viewModel.AppTheme
import com.example.smartfarm.ui.features.settings.presentation.viewModel.SettingsViewModel
import com.example.smartfarm.ui.theme.NegativeRed
import com.example.smartfarm.ui.theme.PositiveGreen
import com.example.smartfarm.ui.theme.SmartFarmTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()
            val settingsViewModel: SettingsViewModel = hiltViewModel()

            val uiState by settingsViewModel.uiState.collectAsState()
            val themePreference = uiState.theme

            val useDarkTheme = when (themePreference) {
                AppTheme.DARK -> true
                AppTheme.LIGHT -> false
                AppTheme.SYSTEM -> isSystemInDarkTheme() // Check the system setting
            }




            SmartFarmTheme (
                darkTheme = useDarkTheme,
                dynamicColor = false
            ){
                NavGraph(
                    navController = navController
                )
            }
        }
    }
}

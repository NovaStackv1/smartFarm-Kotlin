package com.example.smartfarm.ui.features.weather.presentation.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.smartfarm.ui.features.weather.presentation.components.ErrorState
import com.example.smartfarm.ui.features.weather.presentation.components.FarmLocationSelector
import com.example.smartfarm.ui.features.weather.presentation.components.LoadingState
import com.example.smartfarm.ui.features.weather.presentation.components.WeatherContent
import com.example.smartfarm.ui.features.weather.presentation.viewModel.WeatherUiState
import com.example.smartfarm.ui.features.weather.presentation.viewModel.WeatherViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    onNavigateBack: () -> Unit,
    onNavigateToFarmPreferences: () -> Unit,
    weatherViewModel: WeatherViewModel = hiltViewModel()
) {
    val uiState by weatherViewModel.uiState.collectAsState()
    val isRefreshing by weatherViewModel.isRefreshing.collectAsState()
    val currentFarm by weatherViewModel.currentFarm.collectAsState()
    val availableFarms by weatherViewModel.availableFarms.collectAsState()


    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {

        TopAppBar(
            title = {
                Column {
                    Text(
                        "Weather Updates",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    currentFarm?.let { farm ->
                        Text(
                            farm.name,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            navigationIcon = {
                IconButton(
                    onClick = onNavigateBack
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Navigate back"
                    )
                }
            },
            actions = {
                // Farm Location Selector
                if (availableFarms.isNotEmpty()) {
                    FarmLocationSelector(
                        farms = availableFarms,
                        currentFarm = currentFarm,
                        onFarmSelected = { farm -> weatherViewModel.selectFarm(farm) },
                        onManageFarms = onNavigateToFarmPreferences
                    )
                }

                IconButton(
                    onClick = onNavigateToFarmPreferences
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Farm Settings"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = { weatherViewModel.refreshWeather() },
            modifier = Modifier
                .fillMaxSize()
        ) {
            when (val state = uiState) {
                is WeatherUiState.Loading -> {
                    LoadingState()
                }
                is WeatherUiState.Success -> {
                    WeatherContent(weatherData = state.data)
                }
                is WeatherUiState.Error -> {
                    ErrorState(
                        message = state.message,
                        onRetry = { weatherViewModel.refreshWeather() }
                    )
                }
            }
        }
    }
}
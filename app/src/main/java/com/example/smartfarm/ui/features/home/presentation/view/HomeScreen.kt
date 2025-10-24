package com.example.smartfarm.ui.features.home.presentation.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.smartfarm.shared.toSharedWeatherData
import com.example.smartfarm.ui.features.home.model.DashboardData
import com.example.smartfarm.ui.features.home.presentation.components.FarmTipsCard
import com.example.smartfarm.ui.features.home.presentation.components.FinancialSummaryCard
import com.example.smartfarm.ui.features.home.presentation.components.QuickActionsGrid
import com.example.smartfarm.ui.features.home.presentation.components.RecentActivityCard
import com.example.smartfarm.ui.features.home.presentation.components.WeatherCard
import com.example.smartfarm.ui.features.home.presentation.components.WelcomeHeader
import com.example.smartfarm.ui.features.home.viewModel.DashboardState
import com.example.smartfarm.ui.features.home.viewModel.DashboardViewModel
import com.example.smartfarm.ui.features.weather.presentation.viewModel.WeatherViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit,
    viewModel: DashboardViewModel = hiltViewModel(),
    weatherViewModel: WeatherViewModel = hiltViewModel()
) {
    val dashboardState by viewModel.dashboardState.collectAsState()
    val weatherState by weatherViewModel.uiState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    LaunchedEffect(Unit) {
        weatherViewModel.loadWeatherByCurrentLocation()
    }

    val sharedWeatherData = when (weatherState) {
        is com.example.smartfarm.ui.features.weather.presentation.viewModel.WeatherUiState.Success -> {
            (weatherState as com.example.smartfarm.ui.features.weather.presentation.viewModel.WeatherUiState.Success)
                .data.toSharedWeatherData()
        }
        else -> null
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = { viewModel.refresh() },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = dashboardState) {
                is DashboardState.Loading -> {
                    LoadingState()
                }

                is DashboardState.Success -> {
                    DashboardContent(
                        data = state.data,
                        onNavigate = onNavigate,
                    )
                }

                is DashboardState.Error -> {
                    ErrorState(
                        message = state.message,
                        onRetry = { viewModel.loadDashboardData() }
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun DashboardContent(
    data: DashboardData,
    onNavigate: (String) -> Unit,
//    onMenuClick: () -> Unit
) {

    val user = FirebaseAuth.getInstance().currentUser
    val userName = user?.displayName ?: "Friend"

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        item {
            WelcomeHeader(
                userName = userName,
//                onMenuClick = onMenuClick
            )
        }

        item {
            WeatherCard(weather = data.weather)
        }

        item {
            FinancialSummaryCard(
                financial = data.financialSummary,
                onViewDetailsClick = { onNavigate("finance") }
            )
        }

        item {
            QuickActionsGrid(
                actions = data.quickActions,
                onActionClick = onNavigate
            )
        }

        item {
            RecentActivityCard(
                activities = data.recentActivities,
                onViewAllClick = { onNavigate("finance") }
            )
        }

        item {
            FarmTipsCard(tips = data.farmTips)
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Loading your farm data...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "Oops! Something went wrong",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.error
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Retry")
            }
        }
    }
}
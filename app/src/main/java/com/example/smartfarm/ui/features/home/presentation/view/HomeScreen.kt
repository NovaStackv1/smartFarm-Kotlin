package com.example.smartfarm.ui.features.home.presentation.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.smartfarm.ui.features.finance.domain.model.FinancialSummary
import com.example.smartfarm.ui.features.home.model.ActivityType
import com.example.smartfarm.ui.features.home.model.DashboardData
import com.example.smartfarm.ui.features.home.model.FarmTip
import com.example.smartfarm.ui.features.home.model.QuickAction
import com.example.smartfarm.ui.features.home.model.RecentActivity
import com.example.smartfarm.ui.features.home.model.TipPriority
import com.example.smartfarm.ui.features.home.presentation.components.DashboardContent
import com.example.smartfarm.ui.features.home.presentation.components.ErrorState
import com.example.smartfarm.ui.features.home.presentation.components.LoadingState
import com.example.smartfarm.ui.features.weather.presentation.viewModel.WeatherUiState
import com.example.smartfarm.ui.features.weather.presentation.viewModel.WeatherViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.firebase.auth.FirebaseAuth


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit,
    weatherViewModel: WeatherViewModel = hiltViewModel()
) {
    val weatherState by weatherViewModel.uiState.collectAsState()
    val isRefreshing by weatherViewModel.isRefreshing.collectAsState()

    LaunchedEffect(Unit) {
        weatherViewModel.loadWeatherByCurrentLocation()
    }


    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = { weatherViewModel.refreshWeather() },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = weatherState) {
                is WeatherUiState.Loading -> {
                    LoadingState()
                }

                is WeatherUiState.Success -> {
                    // Convert weather data to dashboard data
                    val dashboardData = createDashboardData(state.data)
                    DashboardContent(
                        data = dashboardData,
                        onNavigate = onNavigate,
                    )
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

/**
 * Creates DashboardData from WeatherData and other sources
 */
private fun createDashboardData(weatherData: com.example.smartfarm.ui.features.weather.domain.models.WeatherData): DashboardData {
    val user = FirebaseAuth.getInstance().currentUser
    val userName = user?.displayName ?: "Farmer"

    val isDayTime = isCurrentlyDayTime()
    val financialSummary = getRealFinancialSummary()

    return DashboardData(
        userName = userName,
        weather = weatherData,
        financialSummary = financialSummary,
        recentActivities = getRecentActivities(),
        farmTips = generateFarmTipsFromWeather(weatherData),
        quickActions = getQuickActions()
    )
}

private fun getRealFinancialSummary(): FinancialSummary {
    // You can either:
    // 1. Inject FinanceViewModel in HomeScreen and get data
    // 2. Create a separate use case for home screen financial data
    // 3. Use a shared repository

    // For now, return empty - we'll implement this properly
    return FinancialSummary()
}

// Simple heuristic to determine day/night
private fun isCurrentlyDayTime(): Boolean {
    val currentHour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
    return currentHour in 6..18 // 6 AM to 6 PM considered daytime
}


/**
 * Mock data - replace with actual data from your finance module
 */
//private fun getFinancialSummary(): FinancialSummary {
//    return FinancialSummary(
//        balance = 12500.0,
//        revenue = 18000.0,
//        expenses = 5500.0
//    )
//}

/**
 * Mock data - replace with actual data from your finance module
 */
private fun getRecentActivities(): List<RecentActivity> {
    return listOf(
        RecentActivity(
            id = "1",
            title = "Maize Sale",
            amount = 5000.0,
            type = com.example.smartfarm.ui.features.home.model.ActivityType.INCOME,
            date = "Today",
            category = "Crops"
        ),
        RecentActivity(
            id = "2",
            title = "Fertilizer Purchase",
            amount = 1200.0,
            type = ActivityType.EXPENSE,
            date = "Yesterday",
            category = "Supplies"
        )
    )
}

/**
 * Generate farm tips based on current weather conditions
 */
private fun generateFarmTipsFromWeather(weatherData: com.example.smartfarm.ui.features.weather.domain.models.WeatherData): List<FarmTip> {
    val tips = mutableListOf<FarmTip>()

    // Add farming advice from weather data
    weatherData.farmingAdvice.take(2).forEachIndexed { index, advice ->
        tips.add(
            FarmTip(
                id = "weather_$index",
                title = when (advice.type) {
                    com.example.smartfarm.ui.features.weather.domain.models.AdviceType.POSITIVE -> "Good Opportunity"
                    com.example.smartfarm.ui.features.weather.domain.models.AdviceType.WARNING -> "Important Notice"
                    com.example.smartfarm.ui.features.weather.domain.models.AdviceType.INFO -> "Farm Tip"
                },
                description = advice.message,
                priority = when (advice.priority) {
                    1 -> TipPriority.HIGH
                    2 -> TipPriority.MEDIUM
                    else -> TipPriority.LOW
                }
            )
        )
    }

    // Add general tips if we don't have enough from weather
    if (tips.size < 2) {
        tips.add(
            FarmTip(
                id = "general_1",
                title = "Crop Rotation",
                description = "Consider rotating your crops to maintain soil health",
                priority = TipPriority.MEDIUM
            )
        )
    }

    return tips
}

/**
 * Quick actions for navigation
 */
private fun getQuickActions(): List<QuickAction> {
    return listOf(
        QuickAction(
            id = "weather",
            title = "Weather",
            icon = androidx.compose.material.icons.Icons.Default.Cloud,
            route = "weather"
        ),
        QuickAction(
            id = "finance",
            title = "Finance",
            icon = androidx.compose.material.icons.Icons.Default.AccountBalance,
            route = "expenses"
        ),
        QuickAction(
            id = "farms",
            title = "My Farms",
            icon = androidx.compose.material.icons.Icons.Default.Agriculture,
            route = "farm_preferences"
        ),
        QuickAction(
            id = "tasks",
            title = "Tasks",
            icon = androidx.compose.material.icons.Icons.Default.Checklist,
            route = "tasks"
        )
    )
}

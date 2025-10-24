package com.example.smartfarm.ui.features.home.viewModel

import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfarm.ui.features.auth.data.repo.AuthRepository
import com.example.smartfarm.ui.features.weather.data.repo.WeatherRepository
import com.example.smartfarm.ui.features.weather.data.remote.ApiResult
import com.example.smartfarm.ui.features.home.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val weatherRepository: WeatherRepository,
    // in future: private val financeRepository: FinanceRepository
) : ViewModel() {

    private val _dashboardState = MutableStateFlow<DashboardState>(DashboardState.Loading)
    val dashboardState: StateFlow<DashboardState> = _dashboardState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            _dashboardState.value = DashboardState.Loading

            try {
                // 1️⃣ Fetch username from AuthRepository (Firebase / UserPrefs)
                val userName = authRepository.currentUser?.displayName
                    ?: "Farmer"

                // 2️⃣ Fetch Weather Data (Realtime via API)
                var weatherData: WeatherData? = null
                weatherRepository.getWeatherByLocation("Nairobi").collect { result ->
                    when (result) {
                        is ApiResult.Success -> weatherData = result.data
                        is ApiResult.Error -> throw Exception(result.message)
                        else -> {}
                    }
                }

                // 3️⃣ Fetch Financial Summary (for now, placeholder logic)
                val financialSummary = FinancialOverview(
                    totalIncome = 50000.0,
                    totalExpenses = 20000.0,
                    netProfit = 30000.0,
                    profitPercentage = 60f
                )
                // Later, connect to FinanceRepository to calculate this

                // 4️⃣ Build final Dashboard data object
                val dashboardData = DashboardData(
                    userName = userName,
                    weather = weatherData ?: WeatherData(
                        temperature = 0,
                        condition = "N/A",
                        location = "Unknown",
                        humidity = 0,
                        recommendation = "No data available",
                        icon = WeatherIcon.CLOUDY
                    ),
                    financialSummary = financialSummary,
                    recentActivities = emptyList(), // TODO: connect later
                    farmTips = emptyList(), // can stay dynamic/static
                    quickActions = defaultQuickActions()
                )

                _dashboardState.value = DashboardState.Success(dashboardData)

            } catch (e: Exception) {
                _dashboardState.value = DashboardState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            loadDashboardData()
            _isRefreshing.value = false
        }
    }

    private fun defaultQuickActions(): List<QuickAction> = listOf(
        QuickAction("finances", "Finances", androidx.compose.material.icons.Icons.Default.AccountBalance, "finance"),
        QuickAction("weather", "Weather", androidx.compose.material.icons.Icons.Default.Cloud, "weather"),
        QuickAction("settings", "Settings", androidx.compose.material.icons.Icons.Default.Settings, "settings"),
        QuickAction("profile", "Profile", androidx.compose.material.icons.Icons.Default.AccountCircle, "account")
    )
}

sealed class DashboardState {
    object Loading : DashboardState()
    data class Success(val data: DashboardData) : DashboardState()
    data class Error(val message: String) : DashboardState()
}

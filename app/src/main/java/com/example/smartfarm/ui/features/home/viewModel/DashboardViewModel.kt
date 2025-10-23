package com.example.smartfarm.ui.features.home.viewModel


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfarm.ui.features.home.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    // TODO: Inject repositories when ready
    // private val weatherRepository: WeatherRepository,
    // private val financeRepository: FinanceRepository
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
                // Simulate network delay
                delay(1000)

                // TODO: Replace with actual data from repositories
                val mockData = createMockDashboardData()
                _dashboardState.value = DashboardState.Success(mockData)
            } catch (e: Exception) {
                _dashboardState.value = DashboardState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            delay(1500) // Simulate refresh
            loadDashboardData()
            _isRefreshing.value = false
        }
    }

    private fun createMockDashboardData(): DashboardData {
        return DashboardData(
            userName = "John Kamau",
            weather = WeatherData(
                temperature = 24,
                condition = "Partly Cloudy",
                location = "Nairobi, Kenya",
                humidity = 65,
                recommendation = "Good day for planting. Soil moisture is optimal.",
                icon = WeatherIcon.PARTLY_CLOUDY
            ),
            financialSummary = FinancialOverview(
                totalIncome = 50000.0,
                totalExpenses = 20000.0,
                netProfit = 30000.0,
                profitPercentage = 60f
            ),
            recentActivities = listOf(
                RecentActivity(
                    id = "1",
                    title = "Maize Sale",
                    amount = 15000.0,
                    type = ActivityType.INCOME,
                    date = "Today",
                    category = "Crop Sale"
                ),
                RecentActivity(
                    id = "2",
                    title = "Fertilizer Purchase",
                    amount = 4500.0,
                    type = ActivityType.EXPENSE,
                    date = "Yesterday",
                    category = "Fertilizer"
                ),
                RecentActivity(
                    id = "3",
                    title = "Seeds",
                    amount = 2800.0,
                    type = ActivityType.EXPENSE,
                    date = "2 days ago",
                    category = "Seeds"
                )
            ),
            farmTips = listOf(
                FarmTip(
                    id = "1",
                    title = "Pest Alert",
                    description = "Aphid activity detected. Consider organic pest control.",
                    priority = TipPriority.HIGH
                ),
                FarmTip(
                    id = "2",
                    title = "Optimal Planting",
                    description = "Soil temperature ideal for bean planting this week.",
                    priority = TipPriority.MEDIUM
                ),
                FarmTip(
                    id = "3",
                    title = "Water Management",
                    description = "Rain expected in 3 days. Delay irrigation.",
                    priority = TipPriority.LOW
                )
            ),
            quickActions = listOf(
                QuickAction(
                    id = "finances",
                    title = "Finances",
                    icon = Icons.Default.AccountBalance,
                    route = "finance"
                ),
                QuickAction(
                    id = "weather",
                    title = "Weather",
                    icon = Icons.Default.Cloud,
                    route = "weather"
                ),
                QuickAction(
                    id = "crops",
                    title = "Crops",
                    icon = Icons.Default.Agriculture,
                    route = "crops"
                ),
                QuickAction(
                    id = "market",
                    title = "Market",
                    icon = Icons.Default.Store,
                    route = "market"
                )
            )
        )
    }
}

sealed class DashboardState {
    object Loading : DashboardState()
    data class Success(val data: DashboardData) : DashboardState()
    data class Error(val message: String) : DashboardState()
}
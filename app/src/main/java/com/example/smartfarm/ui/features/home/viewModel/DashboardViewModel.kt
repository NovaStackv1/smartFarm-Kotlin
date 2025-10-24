package com.example.smartfarm.ui.features.home.viewModel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfarm.ui.features.home.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DashboardViewModel : ViewModel() {

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

                val dashboardData = getSampleDashboardData()
                _dashboardState.value = DashboardState.Success(dashboardData)
            } catch (e: Exception) {
                _dashboardState.value = DashboardState.Error(e.message ?: "Failed to load dashboard data")
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                // Simulate refresh delay
                delay(500)
                loadDashboardData()
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    private fun getSampleDashboardData(): DashboardData {
        return DashboardData(
            userName = "John Farmer",
            weather = WeatherData(
                temperature = 25,
                condition = "Sunny",
                location = "Nairobi Farm",
                humidity = 65,
                recommendation = "Perfect weather for irrigation. Consider watering crops in the morning.",
                icon = WeatherIcon.SUNNY
            ),
            financialSummary = FinancialSummary(
                balance = 12500.0,
                revenue = 8500.0,
                expenses = 3200.0
            ),
            recentActivities = getSampleRecentActivities(),
            farmTips = getSampleFarmTips(),
            quickActions = getSampleQuickActions()
        )
    }

    private fun getSampleRecentActivities(): List<RecentActivity> {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return listOf(
            RecentActivity(
                id = "1",
                title = "Maize Sale",
                amount = 5000.0,
                type = ActivityType.INCOME,
                date = dateFormat.format(Date(System.currentTimeMillis() - 86400000)), // Yesterday
                category = "Crops"
            ),
            RecentActivity(
                id = "2",
                title = "Fertilizer Purchase",
                amount = 1200.0,
                type = ActivityType.EXPENSE,
                date = dateFormat.format(Date(System.currentTimeMillis() - 172800000)), // 2 days ago
                category = "Supplies"
            ),
            RecentActivity(
                id = "3",
                title = "Bean Harvest",
                amount = 3500.0,
                type = ActivityType.INCOME,
                date = dateFormat.format(Date(System.currentTimeMillis() - 259200000)), // 3 days ago
                category = "Crops"
            ),
            RecentActivity(
                id = "4",
                title = "Irrigation System Maintenance",
                amount = 800.0,
                type = ActivityType.EXPENSE,
                date = dateFormat.format(Date(System.currentTimeMillis() - 345600000)), // 4 days ago
                category = "Equipment"
            )
        )
    }

    private fun getSampleFarmTips(): List<FarmTip> {
        return listOf(
            FarmTip(
                id = "1",
                title = "Soil Testing",
                description = "Test your soil pH and nutrient levels before planting season to optimize fertilizer use.",
                priority = TipPriority.HIGH
            ),
            FarmTip(
                id = "2",
                title = "Water Conservation",
                description = "Consider drip irrigation to reduce water usage by up to 50% compared to traditional methods.",
                priority = TipPriority.MEDIUM
            ),
            FarmTip(
                id = "3",
                title = "Crop Rotation",
                description = "Rotate your crops annually to prevent soil depletion and reduce pest problems.",
                priority = TipPriority.MEDIUM
            ),
            FarmTip(
                id = "4",
                title = "Weather Monitoring",
                description = "Check weather forecasts regularly to plan irrigation and protect crops from extreme conditions.",
                priority = TipPriority.LOW
            )
        )
    }

    private fun getSampleQuickActions(): List<QuickAction> {
        return listOf(
            QuickAction(
                id = "1",
                title = "Irrigation",
                icon = Icons.Default.WaterDrop,
                route = "irrigation"
            ),
            QuickAction(
                id = "2",
                title = "Crops",
                icon = Icons.Default.Grass,
                route = "crops"
            ),
            QuickAction(
                id = "3",
                title = "Finance",
                icon = Icons.Default.AccountBalance,
                route = "finance"
            ),
            QuickAction(
                id = "4",
                title = "Weather",
                icon = Icons.Default.Cloud,
                route = "weather"
            ),
            QuickAction(
                id = "5",
                title = "Inventory",
                icon = Icons.Default.Inventory,
                route = "inventory"
            ),
            QuickAction(
                id = "6",
                title = "Tasks",
                icon = Icons.Default.Checklist,
                route = "tasks"
            )
        )
    }

    // Method to update financial data (you can call this when new financial data is available)
    fun updateFinancialData(newFinancialSummary: FinancialSummary) {
        val currentState = _dashboardState.value
        if (currentState is DashboardState.Success) {
            val updatedData = currentState.data.copy(financialSummary = newFinancialSummary)
            _dashboardState.value = DashboardState.Success(updatedData)
        }
    }

    // Method to add a new activity
    fun addRecentActivity(activity: RecentActivity) {
        val currentState = _dashboardState.value
        if (currentState is DashboardState.Success) {
            val currentActivities = currentState.data.recentActivities.toMutableList()
            currentActivities.add(0, activity) // Add to beginning
            val updatedData = currentState.data.copy(recentActivities = currentActivities)
            _dashboardState.value = DashboardState.Success(updatedData)
        }
    }

    // Method to update weather data
    fun updateWeatherData(weather: WeatherData) {
        val currentState = _dashboardState.value
        if (currentState is DashboardState.Success) {
            val updatedData = currentState.data.copy(weather = weather)
            _dashboardState.value = DashboardState.Success(updatedData)
        }
    }
}

sealed class DashboardState {
    object Loading : DashboardState()
    data class Success(val data: DashboardData) : DashboardState()
    data class Error(val message: String) : DashboardState()
}
package com.example.smartfarm.ui.features.home.model


import androidx.compose.ui.graphics.vector.ImageVector
import com.example.smartfarm.ui.features.finance.domain.model.FinancialSummary
import com.example.smartfarm.ui.features.weather.domain.models.WeatherData

data class DashboardData(
    val userName: String,
    val weather: WeatherData,
    val financialSummary: FinancialSummary,
    val recentActivities: List<RecentActivity>,
    val farmTips: List<FarmTip>,
    val quickActions: List<QuickAction>
)

data class FinancialOverview(
    val totalIncome: Double,
    val totalExpenses: Double,
    val netProfit: Double,
    val profitPercentage: Float // 0-100 for progress bar
)

data class RecentActivity(
    val id: String,
    val title: String,
    val amount: Double,
    val type: ActivityType,
    val date: String,
    val category: String
)

enum class ActivityType {
    INCOME,
    EXPENSE
}

data class FarmTip(
    val id: String,
    val title: String,
    val description: String,
    val priority: TipPriority
)

enum class TipPriority {
    HIGH,
    MEDIUM,
    LOW
}

data class QuickAction(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val route: String
)



//data class FinancialSummary(
//    val balance: Double,
//    val revenue: Double,
//    val expenses: Double
//)f




package com.example.smartfarm.ui.features.home.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartfarm.ui.features.home.model.DashboardData
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardContent(
    data: DashboardData,
    onNavigate: (String) -> Unit,
) {
    val user = FirebaseAuth.getInstance().currentUser
    val userName = user?.displayName ?: "Friend"

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        item {
            WelcomeHeader(userName = userName)
        }

        item {
            WeatherCard(weather = data.weather)
        }

        item {
            FinancialSummaryCard(
                financial = data.financialSummary,
                onViewDetailsClick = { onNavigate("expenses") }
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
                onViewAllClick = { onNavigate("expenses") }
            )
        }

        item {
            FarmTipsCard(tips = data.farmTips)
        }
    }
}
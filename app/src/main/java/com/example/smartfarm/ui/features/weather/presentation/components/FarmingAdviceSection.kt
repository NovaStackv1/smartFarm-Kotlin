package com.example.smartfarm.ui.features.weather.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.smartfarm.ui.features.weather.domain.models.FarmingAdvice

@Composable
fun FarmingAdviceSection(
    adviceList: List<FarmingAdvice>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        adviceList.forEach { advice ->
            FarmingAdviceCard(advice = advice)
        }
    }
}
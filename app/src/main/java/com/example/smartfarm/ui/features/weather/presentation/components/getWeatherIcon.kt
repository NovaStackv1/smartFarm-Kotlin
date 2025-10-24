package com.example.smartfarm.ui.features.weather.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector

fun getWeatherIcon(condition: String): ImageVector {
    return when {
        condition.contains("sunny", ignoreCase = true) ||
        condition.contains("clear", ignoreCase = true) -> Icons.Default.WbSunny
        condition.contains("cloud", ignoreCase = true) -> Icons.Default.CloudQueue
        condition.contains("rain", ignoreCase = true) ||
        condition.contains("drizzle", ignoreCase = true) -> Icons.Default.Thunderstorm
        condition.contains("storm", ignoreCase = true) ||
        condition.contains("thunder", ignoreCase = true) -> Icons.Default.Thunderstorm
        else -> Icons.Default.Cloud
    }
}
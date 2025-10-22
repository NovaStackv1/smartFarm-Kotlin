package com.example.smartfarm.ui.features.weather.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.smartfarm.ui.features.weather.domain.models.ForecastDayUi

@Composable
fun ForecastSection(forecasts: List<ForecastDayUi>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(forecasts) { forecast ->
            ForecastCard(forecast = forecast)
        }
    }
}
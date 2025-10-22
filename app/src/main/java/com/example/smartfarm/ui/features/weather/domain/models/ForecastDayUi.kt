package com.example.smartfarm.ui.features.weather.domain.models

data class ForecastDayUi(
    val dayName: String,
    val date: String,
    val maxTemp: Int,
    val minTemp: Int,
    val condition: String,
    val conditionCode: Int,
    val precipChance: Int,
    val humidity: Int
)
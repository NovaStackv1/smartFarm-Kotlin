package com.example.smartfarm.ui.features.weather.domain.models

data class WeatherData(
    val location: String,
    val region: String,
    val country: String,
    val currentTemp: Int,
    val condition: String,
    val humidity: Int,
    val windSpeed: Int,
    val precipChance: Int,
    val feelsLike: Int,
    val uv: Double,
    val forecastDays: List<ForecastDayUi>,
    val farmingAdvice: List<FarmingAdvice>
)
// shared/domain/models/SharedWeatherData.kt
package com.example.smartfarm.shared

import com.example.smartfarm.ui.features.weather.domain.models.FarmingAdvice
import com.example.smartfarm.ui.features.weather.domain.models.ForecastDayUi

data class SharedWeatherData(
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
    val farmingAdvice: List<FarmingAdvice>,
    val recommendation: String = "" // For home screen compatibility
)

// Extension to convert from your existing WeatherData
fun com.example.smartfarm.ui.features.weather.domain.models.WeatherData.toSharedWeatherData(): SharedWeatherData {
    return SharedWeatherData(
        location = location,
        region = region,
        country = country,
        currentTemp = currentTemp,
        condition = condition,
        humidity = humidity,
        windSpeed = windSpeed,
        precipChance = precipChance,
        feelsLike = feelsLike,
        uv = uv,
        forecastDays = forecastDays,
        farmingAdvice = farmingAdvice,
        recommendation = farmingAdvice.firstOrNull()?.message ?: "Good weather for farming activities."
    )
}
package com.example.smartfarm.ui.features.home.data.mappers

import com.example.smartfarm.ui.features.home.model.WeatherIcon
import com.example.smartfarm.ui.features.weather.domain.models.WeatherData

/**
 * Converts weather package WeatherData to home screen compatible data
 */
fun WeatherData.toHomeWeatherData(): com.example.smartfarm.ui.features.home.model.WeatherData {
    return com.example.smartfarm.ui.features.home.model.WeatherData(
        temperature = this.currentTemp,
        condition = this.condition,
        location = this.location,
        humidity = this.humidity,
        recommendation = getWeatherRecommendation(this),
        icon = mapWeatherConditionToIcon(this.condition)
    )
}

/**
 * Generates farming recommendations from weather data
 */
private fun getWeatherRecommendation(weatherData: WeatherData): String {
    return if (weatherData.farmingAdvice.isNotEmpty()) {
        // Use the highest priority farming advice
        weatherData.farmingAdvice.sortedBy { it.priority }.first().message
    } else {
        // Fallback recommendation based on weather
        when {
            weatherData.precipChance > 60 -> "Good day for planting. Soil will be moist."
            weatherData.currentTemp > 30 -> "Consider irrigation and shading for crops."
            weatherData.currentTemp < 15 -> "Protect sensitive plants from cold."
            else -> "Good weather for farm activities."
        }
    }
}

/**
 * Maps weather condition string to WeatherIcon
 */
private fun mapWeatherConditionToIcon(condition: String): WeatherIcon {
    return when {
        condition.contains("sun", ignoreCase = true) -> WeatherIcon.SUNNY
        condition.contains("partly", ignoreCase = true) -> WeatherIcon.PARTLY_CLOUDY
        condition.contains("cloud", ignoreCase = true) -> WeatherIcon.CLOUDY
        condition.contains("rain", ignoreCase = true) || 
        condition.contains("shower", ignoreCase = true) -> WeatherIcon.RAINY
        condition.contains("storm", ignoreCase = true) || 
        condition.contains("thunder", ignoreCase = true) -> WeatherIcon.STORMY
        else -> WeatherIcon.SUNNY // default
    }
}
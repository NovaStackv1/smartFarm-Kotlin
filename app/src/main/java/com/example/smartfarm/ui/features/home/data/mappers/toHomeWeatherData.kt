package com.example.smartfarm.ui.features.home.data.mappers

import com.example.smartfarm.ui.features.home.model.WeatherIcon
import com.example.smartfarm.ui.features.weather.domain.models.WeatherData

/**
 * Converts weather package WeatherData to home screen compatible data
 */
fun WeatherData.toHomeWeatherData(isDayTime: Boolean = true): com.example.smartfarm.ui.features.home.model.WeatherData {
    return com.example.smartfarm.ui.features.home.model.WeatherData(
        temperature = this.currentTemp,
        condition = this.condition,
        location = this.location,
        humidity = this.humidity,
        recommendation = getWeatherRecommendation(this),
        icon = mapWeatherConditionToIcon(this.condition, isDayTime)
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
private fun mapWeatherConditionToIcon(condition: String, isDayTime: Boolean = true): WeatherIcon {
    val lowerCondition = condition.lowercase()

    return when {
        // Clear conditions - different icons for day/night
        lowerCondition.contains("clear") -> if (isDayTime) WeatherIcon.SUNNY else WeatherIcon.MOON
        lowerCondition.contains("sunny") -> WeatherIcon.SUNNY

        // Partly cloudy - different icons for day/night
        lowerCondition.contains("partly") -> {
            if (isDayTime) WeatherIcon.PARTLY_CLOUDY_DAY else WeatherIcon.PARTLY_CLOUDY_NIGHT
        }
        lowerCondition.contains("mostly sunny") -> WeatherIcon.PARTLY_CLOUDY_DAY
        lowerCondition.contains("mostly clear") -> {
            if (isDayTime) WeatherIcon.PARTLY_CLOUDY_DAY else WeatherIcon.PARTLY_CLOUDY_NIGHT
        }

        // Cloudy conditions
        lowerCondition.contains("cloudy") || lowerCondition.contains("overcast") -> WeatherIcon.CLOUDY

        // Rain conditions
        lowerCondition.contains("rain") || lowerCondition.contains("drizzle") ||
                lowerCondition.contains("shower") -> WeatherIcon.RAINY

        // Storm conditions
        lowerCondition.contains("storm") || lowerCondition.contains("thunder") -> WeatherIcon.STORMY

        // Snow conditions
        lowerCondition.contains("snow") || lowerCondition.contains("flurries") -> WeatherIcon.SNOWY

        // Fog conditions
        lowerCondition.contains("fog") || lowerCondition.contains("haze") -> WeatherIcon.FOGGY

        // Windy conditions
        lowerCondition.contains("wind") -> WeatherIcon.WINDY

        // Default based on time of day
        else -> if (isDayTime) WeatherIcon.SUNNY else WeatherIcon.MOON
    }
}
package com.example.smartfarm.ui.features.weather.presentation.components

import com.example.smartfarm.ui.features.weather.domain.models.WeatherIcon

/**
 * Maps weather condition string to WeatherIcon with day/night awareness
 */
fun mapWeatherConditionToIcon(condition: String, isDayTime: Boolean = true): WeatherIcon {
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
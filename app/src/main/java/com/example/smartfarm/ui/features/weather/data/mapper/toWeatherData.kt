// data/mapper/WeatherMapper.kt
package com.example.smartfarm.ui.features.weather.data.mapper

import com.example.smartfarm.ui.features.weather.data.models.CurrentWeather
import com.example.smartfarm.ui.features.weather.data.models.ForecastDay
import com.example.smartfarm.ui.features.weather.data.models.WeatherResponse
import com.example.smartfarm.ui.features.weather.domain.models.AdviceType
import com.example.smartfarm.ui.features.weather.domain.models.FarmingAdvice
import com.example.smartfarm.ui.features.weather.domain.models.ForecastDayUi
import com.example.smartfarm.ui.features.weather.domain.models.WeatherData
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

fun WeatherResponse.toWeatherData(): WeatherData {
    return WeatherData(
        location = location.name,
        region = location.region,
        country = location.country,
        currentTemp = current.tempC.roundToInt(),
        condition = current.condition.text,
        humidity = current.humidity,
        windSpeed = current.windKph.roundToInt(),
        precipChance = (current.precipMm * 10).roundToInt().coerceIn(0, 100),
        feelsLike = current.feelsLikeC.roundToInt(),
        uv = current.uv,
        forecastDays = forecast.forecastDay.map { it.toForecastDayUi() },
        farmingAdvice = generateFarmingAdvice(current, forecast.forecastDay)
    )
}

fun ForecastDay.toForecastDayUi(): ForecastDayUi {
    val dayName = date.toDayName()
    return ForecastDayUi(
        dayName = dayName,
        date = date,
        maxTemp = day.maxTempC.roundToInt(),
        minTemp = day.minTempC.roundToInt(),
        condition = day.condition.text,
        conditionCode = day.condition.code,
        precipChance = day.dailyChanceOfRain,
        humidity = day.avgHumidity
    )
}

private fun String.toDayName(): String {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val date = sdf.parse(this)
        val dayFormat = SimpleDateFormat("EEE", Locale.ENGLISH)
        date?.let { dayFormat.format(it) } ?: this
    } catch (e: Exception) {
        this
    }
}

private fun generateFarmingAdvice(
    current: CurrentWeather,
    forecast: List<ForecastDay>
): List<FarmingAdvice> {
    val advice = mutableListOf<FarmingAdvice>()
    
    // Check today's weather
    val todayCondition = current.condition.code
    
    // Sunny day advice (codes: 1000)
    if (todayCondition == 1000) {
        advice.add(
            FarmingAdvice(
                message = "Sunny day ahead. It's a good time for planting maize and beans.",
                type = AdviceType.POSITIVE,
                priority = 1
            )
        )
    }
    
    // High humidity warning
    if (current.humidity > 70) {
        advice.add(
            FarmingAdvice(
                message = "With high humidity, remember to check crops for early signs of fungal diseases.",
                type = AdviceType.WARNING,
                priority = 2
            )
        )
    }
    
    // Check upcoming warm days
    val upcomingDays = forecast.take(3)
    val hasWarmDays = upcomingDays.any { it.day.maxTempC > 28 }
    
    if (hasWarmDays) {
        advice.add(
            FarmingAdvice(
                message = "Ensure your irrigation systems are ready for the warmer days ahead.",
                type = AdviceType.INFO,
                priority = 3
            )
        )
    }
    
    // Rain forecast advice
    val upcomingRain = upcomingDays.any { it.day.dailyChanceOfRain > 60 }
    if (upcomingRain) {
        advice.add(
            FarmingAdvice(
                message = "Heavy rain expected soon. Postpone fertilizer application and prepare drainage.",
                type = AdviceType.WARNING,
                priority = 1
            )
        )
    }
    
    // Low temperature warning
    val coldDays = upcomingDays.any { it.day.minTempC < 12 }
    if (coldDays) {
        advice.add(
            FarmingAdvice(
                message = "Cool temperatures expected. Protect sensitive seedlings and young plants.",
                type = AdviceType.WARNING,
                priority = 2
            )
        )
    }
    
    // UV index advice
    if (current.uv > 6) {
        advice.add(
            FarmingAdvice(
                message = "High UV index today. Good for drying harvested crops, but protect yourself.",
                type = AdviceType.INFO,
                priority = 4
            )
        )
    }
    
    // Dry period advice
    val dryPeriod = upcomingDays.all { it.day.dailyChanceOfRain < 20 }
    if (dryPeriod && current.precipMm < 1) {
        advice.add(
            FarmingAdvice(
                message = "Dry spell expected. Plan irrigation schedule and mulch to retain soil moisture.",
                type = AdviceType.INFO,
                priority = 2
            )
        )
    }
    
    // Optimal conditions for harvesting
    if (todayCondition == 1000 && current.humidity < 60 && current.windKph < 20) {
        advice.add(
            FarmingAdvice(
                message = "Perfect conditions for harvesting and threshing grain crops.",
                type = AdviceType.POSITIVE,
                priority = 1
            )
        )
    }
    
    return advice.sortedBy { it.priority }.take(5)
}
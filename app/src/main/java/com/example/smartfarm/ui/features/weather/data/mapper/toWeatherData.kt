package com.example.smartfarm.ui.features.weather.data.mapper

import com.example.smartfarm.ui.features.weather.data.models.AccuWeatherCurrentConditions
import com.example.smartfarm.ui.features.weather.data.models.AccuWeatherForecastResponse
import com.example.smartfarm.ui.features.weather.domain.models.AdviceType
import com.example.smartfarm.ui.features.weather.domain.models.FarmingAdvice
import com.example.smartfarm.ui.features.weather.domain.models.ForecastDayUi
import com.example.smartfarm.ui.features.weather.domain.models.WeatherData
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

fun AccuWeatherForecastResponse.toWeatherData(
    locationName: String,
    currentConditions: AccuWeatherCurrentConditions? = null
): WeatherData {
    val currentTemp = currentConditions?.Temperature?.Metric?.Value?.roundToInt() ?: 0
    val condition = currentConditions?.WeatherText ?: DailyForecasts.firstOrNull()?.Day?.IconPhrase ?: "Unknown"
    val humidity = currentConditions?.RelativeHumidity ?: 50
    val windSpeed = currentConditions?.Wind?.Speed?.Value?.roundToInt() ?: 0
    val precipChance = DailyForecasts.firstOrNull()?.Day?.PrecipitationProbability ?: 0
    val feelsLike = currentConditions?.RealFeelTemperature?.Metric?.Value?.roundToInt() ?: currentTemp
    val uv = currentConditions?.UVIndex?.toDouble() ?: 0.0

    return WeatherData(
        location = locationName,
        region = "", // AccuWeather doesn't provide this directly in forecast
        country = "", // We'll get this from location search
        currentTemp = currentTemp,
        condition = condition,
        humidity = humidity,
        windSpeed = windSpeed,
        precipChance = precipChance,
        feelsLike = feelsLike,
        uv = uv,
        forecastDays = DailyForecasts.map { it.toForecastDayUi() },
        farmingAdvice = generateAccuWeatherFarmingAdvice(currentConditions, DailyForecasts)
    )
}

fun com.example.smartfarm.ui.features.weather.data.models.DailyForecast.toForecastDayUi(): ForecastDayUi {
    val dayName = Date.toDayName()
    val maxTemp = Temperature.Maximum.toCelsius().roundToInt()
    val minTemp = Temperature.Minimum.toCelsius().roundToInt()

    return ForecastDayUi(
        dayName = dayName,
        date = Date,
        maxTemp = maxTemp,
        minTemp = minTemp,
        condition = Day.IconPhrase,
        conditionCode = Day.Icon, // Using AccuWeather icon codes
        precipChance = Day.PrecipitationProbability ?: 0,
        humidity = 50 // AccuWeather doesn't provide daily humidity in basic forecast
    )
}

private fun String.toDayName(): String {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.ENGLISH)
        val date = sdf.parse(this)
        val dayFormat = SimpleDateFormat("EEE", Locale.ENGLISH)
        date?.let { dayFormat.format(it) } ?: this
    } catch (e: Exception) {
        // Try alternative format
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val date = sdf.parse(this)
            val dayFormat = SimpleDateFormat("EEE", Locale.ENGLISH)
            date?.let { dayFormat.format(it) } ?: this
        } catch (e2: Exception) {
            this
        }
    }
}

private fun generateAccuWeatherFarmingAdvice(
    current: AccuWeatherCurrentConditions?,
    forecast: List<com.example.smartfarm.ui.features.weather.data.models.DailyForecast>
): List<FarmingAdvice> {
    val advice = mutableListOf<FarmingAdvice>()

    val currentTemp = current?.Temperature?.Metric?.Value ?: 0.0
    val currentHumidity = current?.RelativeHumidity ?: 50
    val currentCondition = current?.WeatherText ?: ""
    val currentUV = current?.UVIndex ?: 0

    // Check current conditions
    if (currentCondition.contains("Sunny", ignoreCase = true) ||
        currentCondition.contains("Clear", ignoreCase = true)) {
        advice.add(
            FarmingAdvice(
                message = "Sunny day ahead. It's a good time for planting maize and beans.",
                type = AdviceType.POSITIVE,
                priority = 1
            )
        )
    }

    // High humidity warning
    if (currentHumidity > 70) {
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
    val hasWarmDays = upcomingDays.any { it.Temperature.Maximum.toCelsius() > 28 }

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
    val upcomingRain = upcomingDays.any { it.Day.PrecipitationProbability ?: 0 > 60 }
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
    val coldDays = upcomingDays.any { it.Temperature.Minimum.toCelsius() < 12 }
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
    if (currentUV > 6) {
        advice.add(
            FarmingAdvice(
                message = "High UV index today. Good for drying harvested crops, but protect yourself.",
                type = AdviceType.INFO,
                priority = 4
            )
        )
    }

    // Dry period advice
    val dryPeriod = upcomingDays.all { it.Day.PrecipitationProbability ?: 0 < 20 }
    if (dryPeriod) {
        advice.add(
            FarmingAdvice(
                message = "Dry spell expected. Plan irrigation schedule and mulch to retain soil moisture.",
                type = AdviceType.INFO,
                priority = 2
            )
        )
    }

    // Optimal conditions for harvesting
    if ((currentCondition.contains("Sunny", ignoreCase = true) ||
                currentCondition.contains("Clear", ignoreCase = true)) &&
        currentHumidity < 60 &&
        (current?.Wind?.Speed?.Value ?: 0.0) < 20) {
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
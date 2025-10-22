package com.example.smartfarm.ui.features.weather.data.models

import com.google.gson.annotations.SerializedName
// API Response Models
data class WeatherResponse(
    @SerializedName("location")
    val location: Location,
    @SerializedName("current")
    val current: CurrentWeather,
    @SerializedName("forecast")
    val forecast: Forecast
)

data class Location(
    @SerializedName("name")
    val name: String,
    @SerializedName("region")
    val region: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double,
    @SerializedName("localtime")
    val localtime: String
)

data class CurrentWeather(
    @SerializedName("temp_c")
    val tempC: Double,
    @SerializedName("condition")
    val condition: WeatherCondition,
    @SerializedName("wind_kph")
    val windKph: Double,
    @SerializedName("precip_mm")
    val precipMm: Double,
    @SerializedName("humidity")
    val humidity: Int,
    @SerializedName("feelslike_c")
    val feelsLikeC: Double,
    @SerializedName("uv")
    val uv: Double
)

data class WeatherCondition(
    @SerializedName("text")
    val text: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("code")
    val code: Int
)

data class Forecast(
    @SerializedName("forecastday")
    val forecastDay: List<ForecastDay>
)

data class ForecastDay(
    @SerializedName("date")
    val date: String,
    @SerializedName("day")
    val day: DayWeather,
    @SerializedName("astro")
    val astro: Astro
)

data class DayWeather(
    @SerializedName("maxtemp_c")
    val maxTempC: Double,
    @SerializedName("mintemp_c")
    val minTempC: Double,
    @SerializedName("avgtemp_c")
    val avgTempC: Double,
    @SerializedName("maxwind_kph")
    val maxWindKph: Double,
    @SerializedName("totalprecip_mm")
    val totalPrecipMm: Double,
    @SerializedName("avghumidity")
    val avgHumidity: Int,
    @SerializedName("daily_chance_of_rain")
    val dailyChanceOfRain: Int,
    @SerializedName("condition")
    val condition: WeatherCondition,
    @SerializedName("uv")
    val uv: Double
)

data class Astro(
    @SerializedName("sunrise")
    val sunrise: String,
    @SerializedName("sunset")
    val sunset: String
)
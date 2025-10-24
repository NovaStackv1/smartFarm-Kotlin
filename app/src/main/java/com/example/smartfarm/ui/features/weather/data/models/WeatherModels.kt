package com.example.smartfarm.ui.features.weather.data.models

import com.google.gson.annotations.SerializedName

// AccuWeather Location Search Response
data class AccuWeatherLocation(
    @SerializedName("Key")
    val Key: String,

    @SerializedName("LocalizedName")
    val LocalizedName: String,

    @SerializedName("EnglishName")
    val EnglishName: String,

    @SerializedName("Country")
    val Country: Country,

    @SerializedName("AdministrativeArea")
    val AdministrativeArea: AdministrativeArea,

    @SerializedName("GeoPosition")
    val GeoPosition: GeoPosition
)

data class Country(
    @SerializedName("ID")
    val ID: String,

    @SerializedName("LocalizedName")
    val LocalizedName: String,

    @SerializedName("EnglishName")
    val EnglishName: String
)

data class AdministrativeArea(
    @SerializedName("ID")
    val ID: String,

    @SerializedName("LocalizedName")
    val LocalizedName: String,

    @SerializedName("EnglishName")
    val EnglishName: String
)

data class GeoPosition(
    @SerializedName("Latitude")
    val Latitude: Double,

    @SerializedName("Longitude")
    val Longitude: Double
)

// AccuWeather Forecast Response
data class AccuWeatherForecastResponse(
    @SerializedName("Headline")
    val Headline: Headline,

    @SerializedName("DailyForecasts")
    val DailyForecasts: List<DailyForecast>
)

data class Headline(
    @SerializedName("Text")
    val Text: String,

    @SerializedName("Category")
    val Category: String,

    @SerializedName("EffectiveDate")
    val EffectiveDate: String
)

data class DailyForecast(
    @SerializedName("Date")
    val Date: String,

    @SerializedName("EpochDate")
    val EpochDate: Long,

    @SerializedName("Temperature")
    val Temperature: Temperature,

    @SerializedName("Day")
    val Day: DayNight,

    @SerializedName("Night")
    val Night: DayNight,

    @SerializedName("Sources")
    val Sources: List<String>,

    @SerializedName("MobileLink")
    val MobileLink: String,

    @SerializedName("Link")
    val Link: String
)

data class Temperature(
    @SerializedName("Minimum")
    val Minimum: TemperatureValue,

    @SerializedName("Maximum")
    val Maximum: TemperatureValue
)

data class TemperatureValue(
    @SerializedName("Value")
    val Value: Double,

    @SerializedName("Unit")
    val Unit: String,

    @SerializedName("UnitType")
    val UnitType: Int
) {
    // Convert Fahrenheit to Celsius if needed
    fun toCelsius(): Double {
        return if (Unit == "F") (Value - 32) * 5 / 9 else Value
    }
}

data class DayNight(
    @SerializedName("Icon")
    val Icon: Int,

    @SerializedName("IconPhrase")
    val IconPhrase: String,

    @SerializedName("HasPrecipitation")
    val HasPrecipitation: Boolean,

    @SerializedName("PrecipitationType")
    val PrecipitationType: String?,

    @SerializedName("PrecipitationIntensity")
    val PrecipitationIntensity: String?,

    @SerializedName("PrecipitationProbability")
    val PrecipitationProbability: Int?,

    @SerializedName("Wind")
    val Wind: Wind,

    @SerializedName("WindGust")
    val WindGust: WindGust?
)

data class Wind(
    @SerializedName("Speed")
    val Speed: Speed,

    @SerializedName("Direction")
    val Direction: Direction
)

data class Speed(
    @SerializedName("Value")
    val Value: Double,

    @SerializedName("Unit")
    val Unit: String,

    @SerializedName("UnitType")
    val UnitType: Int
)

data class Direction(
    @SerializedName("Degrees")
    val Degrees: Int,

    @SerializedName("Localized")
    val Localized: String,

    @SerializedName("English")
    val English: String
)

data class WindGust(
    @SerializedName("Speed")
    val Speed: Speed
)

// AccuWeather Current Conditions Response
data class AccuWeatherCurrentConditions(
    @SerializedName("LocalObservationDateTime")
    val LocalObservationDateTime: String,

    @SerializedName("EpochTime")
    val EpochTime: Long,

    @SerializedName("WeatherText")
    val WeatherText: String,

    @SerializedName("WeatherIcon")
    val WeatherIcon: Int,

    @SerializedName("HasPrecipitation")
    val HasPrecipitation: Boolean,

    @SerializedName("PrecipitationType")
    val PrecipitationType: String?,

    @SerializedName("IsDayTime")
    val IsDayTime: Boolean,

    @SerializedName("Temperature")
    val Temperature: MetricImperial,

    @SerializedName("RealFeelTemperature")
    val RealFeelTemperature: MetricImperial,

    @SerializedName("RelativeHumidity")
    val RelativeHumidity: Int,

    @SerializedName("Wind")
    val Wind: Wind,

    @SerializedName("UVIndex")
    val UVIndex: Int,

    @SerializedName("UVIndexText")
    val UVIndexText: String,

    @SerializedName("Visibility")
    val Visibility: MetricImperial,

    @SerializedName("Pressure")
    val Pressure: MetricImperial,

    @SerializedName("MobileLink")
    val MobileLink: String,

    @SerializedName("Link")
    val Link: String
)

data class MetricImperial(
    @SerializedName("Metric")
    val Metric: UnitValue,

    @SerializedName("Imperial")
    val Imperial: UnitValue
)

data class UnitValue(
    @SerializedName("Value")
    val Value: Double,

    @SerializedName("Unit")
    val Unit: String,

    @SerializedName("UnitType")
    val UnitType: Int
)
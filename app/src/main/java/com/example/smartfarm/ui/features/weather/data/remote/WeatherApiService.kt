package com.example.smartfarm.ui.features.weather.data.remote

import com.example.smartfarm.ui.features.weather.data.models.AccuWeatherCurrentConditions
import com.example.smartfarm.ui.features.weather.data.models.AccuWeatherForecastResponse
import com.example.smartfarm.ui.features.weather.data.models.AccuWeatherLocation
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApiService {

    @GET("locations/v1/cities/search")
    suspend fun searchLocation(
        @Query("apikey") apiKey: String,
        @Query("q") location: String
    ): Response<List<AccuWeatherLocation>>

    @GET("locations/v1/cities/geoposition/search")
    suspend fun searchLocationByCoordinates(
        @Query("apikey") apiKey: String,
        @Query("q") coordinates: String // Format: "lat,lon"
    ): Response<AccuWeatherLocation>

    @GET("forecasts/v1/daily/5day/{locationKey}")
    suspend fun getFiveDayForecast(
        @Path("locationKey") locationKey: String,
        @Query("apikey") apiKey: String,
        @Query("metric") metric: Boolean = true,
        @Query("details") details: Boolean = true
    ): Response<AccuWeatherForecastResponse>

    @GET("currentconditions/v1/{locationKey}")
    suspend fun getCurrentConditions(
        @Path("locationKey") locationKey: String,
        @Query("apikey") apiKey: String,
        @Query("details") details: Boolean = true
    ): Response<List<AccuWeatherCurrentConditions>>
}
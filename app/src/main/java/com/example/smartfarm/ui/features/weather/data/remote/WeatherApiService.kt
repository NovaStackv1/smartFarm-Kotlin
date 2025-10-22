package com.example.smartfarm.ui.features.weather.data.remote

import com.example.smartfarm.ui.features.weather.data.models.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    
    @GET("forecast.json")
    suspend fun getWeatherForecast(
        @Query("key") apiKey: String,
        @Query("q") location: String,
        @Query("days") days: Int = 7,
        @Query("aqi") aqi: String = "no",
        @Query("alerts") alerts: String = "no"
    ): Response<WeatherResponse>
    
    @GET("forecast.json")
    suspend fun getWeatherByCoordinates(
        @Query("key") apiKey: String,
        @Query("q") coordinates: String, // Format: "lat,lon"
        @Query("days") days: Int = 7,
        @Query("aqi") aqi: String = "no",
        @Query("alerts") alerts: String = "no"
    ): Response<WeatherResponse>
}
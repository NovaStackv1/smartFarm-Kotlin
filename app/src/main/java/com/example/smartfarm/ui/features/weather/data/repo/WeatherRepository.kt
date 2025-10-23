package com.example.smartfarm.ui.features.weather.data.repo

import com.example.smartfarm.ui.features.weather.data.remote.ApiResult
import com.example.smartfarm.ui.features.weather.domain.models.WeatherData
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeatherByLocation(location: String): Flow<ApiResult<WeatherData>>
    suspend fun getWeatherByCoordinates(lat: Double, lon: Double): Flow<ApiResult<WeatherData>>
}
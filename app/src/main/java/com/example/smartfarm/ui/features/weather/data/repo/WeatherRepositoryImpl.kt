package com.example.smartfarm.ui.features.weather.data.repo

import com.example.smartfarm.BuildConfig
import com.example.smartfarm.ui.features.weather.data.remote.ApiResult
import com.example.smartfarm.ui.features.weather.data.remote.WeatherApiService
import com.example.smartfarm.ui.features.weather.data.remote.safeApiCall
import com.example.smartfarm.ui.features.weather.domain.models.WeatherData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: WeatherApiService
) : WeatherRepository {

    override suspend fun getWeatherByLocation(location: String): Flow<ApiResult<WeatherData>> = flow {
        emit(ApiResult.Loading)
        
        val result = safeApiCall {
            apiService.getWeatherForecast(
                apiKey = BuildConfig.WEATHER_API_KEY,
                location = location,
                days = 7
            )
        }
        
        when (result) {
            is ApiResult.Success -> {
                val weatherData = result.data.toWeatherData()
                emit(ApiResult.Success(weatherData))
            }
            is ApiResult.Error -> {
                emit(result)
            }
            else -> {}
        }
    }

    override suspend fun getWeatherByCoordinates(
        lat: Double,
        lon: Double
    ): Flow<ApiResult<WeatherData>> = flow {
        emit(ApiResult.Loading)
        
        val result = safeApiCall {
            apiService.getWeatherByCoordinates(
                apiKey = BuildConfig.WEATHER_API_KEY,
                coordinates = "$lat,$lon",
                days = 7
            )
        }
        
        when (result) {
            is ApiResult.Success -> {
                val weatherData = result.data.toWeatherData()
                emit(ApiResult.Success(weatherData))
            }
            is ApiResult.Error -> {
                emit(result)
            }
            else -> {}
        }
    }
}
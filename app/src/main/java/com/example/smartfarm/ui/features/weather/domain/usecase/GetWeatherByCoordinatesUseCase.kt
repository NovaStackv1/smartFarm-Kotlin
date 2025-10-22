package com.example.smartfarm.ui.features.weather.domain.usecase

import com.example.smartfarm.ui.features.weather.data.remote.ApiResult
import com.example.smartfarm.ui.features.weather.data.repo.WeatherRepository
import com.example.smartfarm.ui.features.weather.domain.models.WeatherData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeatherByCoordinatesUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(lat: Double, lon: Double): Flow<ApiResult<WeatherData>> {
        return repository.getWeatherByCoordinates(lat, lon)
    }
}
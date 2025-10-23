package com.example.smartfarm.ui.features.weather.domain.usecase

import com.example.smartfarm.ui.features.weather.data.remote.ApiResult
import com.example.smartfarm.ui.features.weather.data.repo.WeatherRepository
import com.example.smartfarm.ui.features.weather.domain.models.WeatherData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeatherByLocationUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(location: String): Flow<ApiResult<WeatherData>> {
        return repository.getWeatherByLocation(location)
    }
}
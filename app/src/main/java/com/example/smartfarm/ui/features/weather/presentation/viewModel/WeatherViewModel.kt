package com.example.smartfarm.ui.features.weather.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfarm.ui.features.weather.data.remote.ApiResult
import com.example.smartfarm.ui.features.weather.domain.models.WeatherData
import com.example.smartfarm.ui.features.weather.domain.usecase.GetCurrentLocationUseCase
import com.example.smartfarm.ui.features.weather.domain.usecase.GetWeatherByCoordinatesUseCase
import com.example.smartfarm.ui.features.weather.domain.usecase.GetWeatherByLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherByLocationUseCase: GetWeatherByLocationUseCase,
    private val getWeatherByCoordinatesUseCase: GetWeatherByCoordinatesUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private var currentLocation: String = "Nairobi" // Default location

    init {
        loadWeatherByCurrentLocation()
    }

    fun loadWeatherByCurrentLocation() {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            
            val locationResult = getCurrentLocationUseCase()
            
            locationResult.onSuccess { location ->
                getWeatherByCoordinatesUseCase(location.lat, location.lon).collect { result ->
                    handleWeatherResult(result)
                }
            }.onFailure {
                // Fallback to default location if location access fails
                loadWeatherByLocation(currentLocation)
            }
        }
    }

    fun loadWeatherByLocation(location: String) {
        currentLocation = location
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            getWeatherByLocationUseCase(location).collect { result ->
                handleWeatherResult(result)
            }
        }
    }

    fun refreshWeather() {
        viewModelScope.launch {
            _isRefreshing.value = true
            getWeatherByLocationUseCase(currentLocation).collect { result ->
                handleWeatherResult(result)
                _isRefreshing.value = false
            }
        }
    }

    private fun handleWeatherResult(result: ApiResult<WeatherData>) {
        when (result) {
            is ApiResult.Success -> {
                _uiState.value = WeatherUiState.Success(result.data)
            }
            is ApiResult.Error -> {
                _uiState.value = WeatherUiState.Error(
                    result.message ?: "An unexpected error occurred"
                )
            }
            is ApiResult.Loading -> {
                _uiState.value = WeatherUiState.Loading
            }
        }
    }
}

sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val data: WeatherData) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}
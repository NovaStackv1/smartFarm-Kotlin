package com.example.smartfarm.ui.features.weather.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfarm.ui.features.farmpreferences.domain.models.Farm
import com.example.smartfarm.ui.features.farmpreferences.domain.usecase.GetFarmsUseCase
import com.example.smartfarm.ui.features.weather.data.remote.ApiResult
import com.example.smartfarm.ui.features.weather.domain.models.WeatherData
import com.example.smartfarm.ui.features.weather.domain.usecase.GetCurrentLocationUseCase
import com.example.smartfarm.ui.features.weather.domain.usecase.GetDefaultFarmUseCase
import com.example.smartfarm.ui.features.weather.domain.usecase.GetWeatherByCoordinatesUseCase
import com.example.smartfarm.ui.features.weather.domain.usecase.GetWeatherByLocationUseCase
import com.google.firebase.auth.FirebaseAuth
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
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val getFarmsUseCase: GetFarmsUseCase,
    private val getDefaultFarmUseCase: GetDefaultFarmUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    //private var currentLocation: String = "Nairobi" // Default location

    private val _currentFarm = MutableStateFlow<Farm?>(null)
    val currentFarm: StateFlow<Farm?> = _currentFarm.asStateFlow()

    private val _availableFarms = MutableStateFlow<List<Farm>>(emptyList())
    val availableFarms: StateFlow<List<Farm>> = _availableFarms.asStateFlow()


    private val userId: String
        get() = firebaseAuth.currentUser?.uid ?: ""

    init {
        loadFarmsAndWeather()
    }

    private fun loadFarmsAndWeather() {
        viewModelScope.launch {
            // Load farms first
            getFarmsUseCase(userId).collect { farms ->
                _availableFarms.value = farms

                // Try to get default farm, otherwise use first farm or current location
                val defaultFarm = getDefaultFarmUseCase(userId)
                if (defaultFarm != null) {
                    _currentFarm.value = defaultFarm
                    loadWeatherForFarm(defaultFarm)
                } else if (farms.isNotEmpty()) {
                    _currentFarm.value = farms.first()
                    loadWeatherForFarm(farms.first())
                } else {
                    // No farms, use current location
                    loadWeatherByCurrentLocation()
                }
            }
        }
    }

    fun selectFarm(farm: Farm) {
        _currentFarm.value = farm
        loadWeatherForFarm(farm)
    }

    private fun loadWeatherForFarm(farm: Farm) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            getWeatherByCoordinatesUseCase(
                farm.location.latitude,
                farm.location.longitude
            ).collect { result ->
                handleWeatherResult(result)
            }
        }
    }

    fun loadWeatherByCurrentLocation() {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            _currentFarm.value = null // Clear farm selection

            val locationResult = getCurrentLocationUseCase()
            locationResult.onSuccess { location ->
                getWeatherByCoordinatesUseCase(location.lat, location.lon).collect { result ->
                    handleWeatherResult(result)
                }
            }.onFailure {
                // Fallback to default location
                loadWeatherByLocation("Nairobi")
            }
        }
    }

    fun loadWeatherByLocation(location: String) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            _currentFarm.value = null // Clear farm selection

            getWeatherByLocationUseCase(location).collect { result ->
                handleWeatherResult(result)
            }
        }
    }

    fun refreshWeather() {
        viewModelScope.launch {
            _isRefreshing.value = true
            currentFarm.value?.let { farm ->
                loadWeatherForFarm(farm)
            } ?: run {
                loadWeatherByCurrentLocation()
            }
            _isRefreshing.value = false
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
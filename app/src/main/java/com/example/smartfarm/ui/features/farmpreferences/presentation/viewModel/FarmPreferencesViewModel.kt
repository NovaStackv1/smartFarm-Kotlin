package com.example.smartfarm.ui.features.farmpreferences.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfarm.ui.features.farmpreferences.domain.models.*
import com.example.smartfarm.ui.features.farmpreferences.domain.usecase.*
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FarmPreferencesViewModel @Inject constructor(
    private val getFarmsUseCase: GetFarmsUseCase,
    private val saveFarmUseCase: SaveFarmUseCase,
    private val setDefaultFarmUseCase: SetDefaultFarmUseCase,
    private val deleteFarmUseCase: DeleteFarmUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow<FarmPreferencesUiState>(FarmPreferencesUiState.Loading)
    val uiState: StateFlow<FarmPreferencesUiState> = _uiState.asStateFlow()

    private val _farms = MutableStateFlow<List<Farm>>(emptyList())
    val farms: StateFlow<List<Farm>> = _farms.asStateFlow()

    private val userId: String
        get() = firebaseAuth.currentUser?.uid ?: ""

    init {
        loadFarms()
    }

    fun loadFarms() {
        viewModelScope.launch {
            getFarmsUseCase(userId).collect { farmsList ->
                _farms.value = farmsList
                _uiState.value = FarmPreferencesUiState.Success
            }
        }
    }

    fun saveFarm(farm: Farm) {
        viewModelScope.launch {
            try {
                saveFarmUseCase(farm, userId)
            } catch (e: Exception) {
                _uiState.value = FarmPreferencesUiState.Error("Failed to save farm: ${e.message}")
            }
        }
    }

    fun setDefaultFarm(farmId: String) {
        viewModelScope.launch {
            try {
                setDefaultFarmUseCase(farmId, userId)
            } catch (e: Exception) {
                _uiState.value = FarmPreferencesUiState.Error("Failed to set default farm: ${e.message}")
            }
        }
    }

    fun deleteFarm(farmId: String) {
        viewModelScope.launch {
            try {
                deleteFarmUseCase(farmId, userId)
            } catch (e: Exception) {
                _uiState.value = FarmPreferencesUiState.Error("Failed to delete farm: ${e.message}")
            }
        }
    }

    fun clearError() {
        _uiState.value = FarmPreferencesUiState.Success
    }
}

sealed class FarmPreferencesUiState {
    object Loading : FarmPreferencesUiState()
    object Success : FarmPreferencesUiState()
    data class Error(val message: String) : FarmPreferencesUiState()
}
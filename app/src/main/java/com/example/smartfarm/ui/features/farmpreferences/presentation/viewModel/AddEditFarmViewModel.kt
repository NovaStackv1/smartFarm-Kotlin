package com.example.smartfarm.ui.features.farmpreferences.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfarm.ui.features.farmpreferences.domain.models.*
import com.example.smartfarm.ui.features.farmpreferences.domain.usecase.GetFarmByIdUseCase
import com.example.smartfarm.ui.features.farmpreferences.domain.usecase.SaveFarmUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddEditFarmViewModel @Inject constructor(
    private val getFarmByIdUseCase: GetFarmByIdUseCase,
    private val saveFarmUseCase: SaveFarmUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditFarmUiState())
    val uiState: StateFlow<AddEditFarmUiState> = _uiState.asStateFlow()

    private val userId: String
        get() = firebaseAuth.currentUser?.uid ?: ""

    fun loadFarm(farmId: String) {
        viewModelScope.launch {
            val farm = getFarmByIdUseCase(farmId, userId)
            farm?.let {
                _uiState.value = _uiState.value.copy(farm = it)
            }
        }
    }

    fun updateFarmName(name: String) {
        _uiState.value = _uiState.value.copy(
            farm = _uiState.value.farm.copy(name = name),
            errors = _uiState.value.errors - FormError.NAME_EMPTY
        )
    }

    fun updateLocation(location: FarmLocation) {
        _uiState.value = _uiState.value.copy(
            farm = _uiState.value.farm.copy(location = location),
            errors = _uiState.value.errors - FormError.LOCATION_EMPTY
        )
    }

    fun updateSize(size: Double) {
        _uiState.value = _uiState.value.copy(
            farm = _uiState.value.farm.copy(size = size)
        )
    }

    fun updateCropTypes(cropTypes: List<CropType>) {
        _uiState.value = _uiState.value.copy(
            farm = _uiState.value.farm.copy(cropTypes = cropTypes)
        )
    }

    fun updateSoilType(soilType: SoilType) {
        _uiState.value = _uiState.value.copy(
            farm = _uiState.value.farm.copy(soilType = soilType)
        )
    }

    fun updateIrrigationMethod(method: IrrigationMethod) {
        _uiState.value = _uiState.value.copy(
            farm = _uiState.value.farm.copy(irrigationMethod = method)
        )
    }

    fun updateIsDefault(isDefault: Boolean) {
        _uiState.value = _uiState.value.copy(
            farm = _uiState.value.farm.copy(isDefault = isDefault)
        )
    }

    fun validateForm(): Boolean {
        val errors = mutableSetOf<FormError>()
        
        if (_uiState.value.farm.name.isBlank()) {
            errors.add(FormError.NAME_EMPTY)
        }
        
        if (_uiState.value.farm.location.name.isBlank()) {
            errors.add(FormError.LOCATION_EMPTY)
        }
        
        _uiState.value = _uiState.value.copy(errors = errors)
        return errors.isEmpty()
    }

    fun saveFarm() {
        if (validateForm()) {
            viewModelScope.launch {
                val farmToSave = if (_uiState.value.farm.id.isBlank()) {
                    _uiState.value.farm.copy(id = UUID.randomUUID().toString())
                } else {
                    _uiState.value.farm
                }
                saveFarmUseCase(farmToSave, userId)
            }
        }
    }
}

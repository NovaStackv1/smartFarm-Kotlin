package com.example.smartfarm.ui.features.farmpreferences.presentation.viewModel

import com.example.smartfarm.ui.features.farmpreferences.domain.models.Farm
import com.example.smartfarm.ui.features.farmpreferences.domain.models.FarmLocation
import com.example.smartfarm.ui.features.farmpreferences.domain.models.IrrigationMethod
import com.example.smartfarm.ui.features.farmpreferences.domain.models.SoilType

data class AddEditFarmUiState(
    val farm: Farm = Farm(
        id = "",
        name = "",
        location = FarmLocation(),
        size = 0.0,
        cropTypes = emptyList(),
        soilType = SoilType.LOAM,
        irrigationMethod = IrrigationMethod.DRIP,
        isDefault = false
    ),
    val errors: Set<FormError> = emptySet()
)

enum class FormError {
    NAME_EMPTY,
    LOCATION_EMPTY
}
package com.example.smartfarm.ui.features.farmpreferences.domain.usecase

import com.example.smartfarm.ui.features.farmpreferences.domain.models.Farm
import com.example.smartfarm.ui.features.farmpreferences.domain.repo.FarmPreferencesRepository
import javax.inject.Inject

class SaveFarmUseCase @Inject constructor(
    private val repository: FarmPreferencesRepository
) {
    suspend operator fun invoke(farm: Farm, userId: String) {
        repository.saveFarm(farm, userId)
    }
}
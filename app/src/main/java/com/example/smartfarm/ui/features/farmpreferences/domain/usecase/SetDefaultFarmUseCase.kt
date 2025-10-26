package com.example.smartfarm.ui.features.farmpreferences.domain.usecase

import com.example.smartfarm.ui.features.farmpreferences.domain.repo.FarmPreferencesRepository
import javax.inject.Inject

class SetDefaultFarmUseCase @Inject constructor(
    private val repository: FarmPreferencesRepository
) {
    suspend operator fun invoke(farmId: String, userId: String) {
        repository.setDefaultFarm(farmId, userId)
    }
}
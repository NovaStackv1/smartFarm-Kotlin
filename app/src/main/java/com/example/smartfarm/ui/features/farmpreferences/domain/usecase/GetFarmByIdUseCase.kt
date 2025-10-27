package com.example.smartfarm.ui.features.farmpreferences.domain.usecase

import com.example.smartfarm.ui.features.farmpreferences.domain.models.Farm
import com.example.smartfarm.ui.features.farmpreferences.domain.repo.FarmPreferencesRepository
import javax.inject.Inject

class GetFarmByIdUseCase @Inject constructor(
    private val repository: FarmPreferencesRepository
) {
    suspend operator fun invoke(farmId: String, userId: String): Farm? {
        return repository.getFarmById(farmId, userId)
    }
}
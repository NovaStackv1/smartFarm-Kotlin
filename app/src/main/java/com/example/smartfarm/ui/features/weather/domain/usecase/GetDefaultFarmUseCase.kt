package com.example.smartfarm.ui.features.weather.domain.usecase

import com.example.smartfarm.ui.features.farmpreferences.domain.models.Farm
import com.example.smartfarm.ui.features.farmpreferences.domain.repo.FarmPreferencesRepository
import javax.inject.Inject

class GetDefaultFarmUseCase @Inject constructor(
    private val repository: FarmPreferencesRepository
) {
    suspend operator fun invoke(userId: String): Farm? {
        return repository.getDefaultFarm(userId)
    }
}
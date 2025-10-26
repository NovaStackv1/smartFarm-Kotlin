package com.example.smartfarm.ui.features.farmpreferences.domain.usecase

import com.example.smartfarm.ui.features.farmpreferences.domain.models.Farm
import com.example.smartfarm.ui.features.farmpreferences.domain.repo.FarmPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFarmsUseCase @Inject constructor(
    private val repository: FarmPreferencesRepository
) {
    operator fun invoke(userId: String): Flow<List<Farm>> {
        return repository.getFarms(userId)
    }
}
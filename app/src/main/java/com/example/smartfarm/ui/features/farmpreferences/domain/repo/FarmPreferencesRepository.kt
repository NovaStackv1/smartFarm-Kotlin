package com.example.smartfarm.ui.features.farmpreferences.domain.repo

import com.example.smartfarm.ui.features.farmpreferences.domain.models.Farm
import kotlinx.coroutines.flow.Flow

interface FarmPreferencesRepository {
    fun getFarms(userId: String): Flow<List<Farm>>
    suspend fun getFarmById(farmId: String, userId: String): Farm?
    suspend fun getDefaultFarm(userId: String): Farm?
    suspend fun saveFarm(farm: Farm, userId: String)
    suspend fun setDefaultFarm(farmId: String, userId: String)
    suspend fun deleteFarm(farmId: String, userId: String)
}
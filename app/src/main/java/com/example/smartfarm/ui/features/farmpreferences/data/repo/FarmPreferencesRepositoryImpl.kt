package com.example.smartfarm.ui.features.farmpreferences.data.repo

import com.example.smartfarm.ui.features.farmpreferences.data.local.dao.FarmDao
import com.example.smartfarm.ui.features.farmpreferences.data.mapper.toDomain
import com.example.smartfarm.ui.features.farmpreferences.data.mapper.toEntity

import com.example.smartfarm.ui.features.farmpreferences.domain.models.Farm
import com.example.smartfarm.ui.features.farmpreferences.domain.repo.FarmPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FarmPreferencesRepositoryImpl @Inject constructor(
    private val farmDao: FarmDao
) : FarmPreferencesRepository {

    override fun getFarms(userId: String): Flow<List<Farm>> {
        return farmDao.getFarmsByUser(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getFarmById(farmId: String, userId: String): Farm? {
        return farmDao.getFarmById(farmId, userId)?.toDomain()
    }

    override suspend fun getDefaultFarm(userId: String): Farm? {
        return farmDao.getDefaultFarm(userId)?.toDomain()
    }

    override suspend fun saveFarm(farm: Farm, userId: String) {
        val entity = farm.toEntity(userId)
        farmDao.insertFarm(entity)
        
        if (farm.isDefault) {
            farmDao.setDefaultFarm(farm.id, userId)
        }
    }

    override suspend fun setDefaultFarm(farmId: String, userId: String) {
        farmDao.clearDefaultFarms(userId)
        farmDao.setDefaultFarm(farmId, userId)
    }

    override suspend fun deleteFarm(farmId: String, userId: String) {
        val farm = farmDao.getFarmById(farmId, userId)
        farm?.let { farmDao.deleteFarm(it) }
    }
}
package com.example.smartfarm.ui.features.farmpreferences.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.smartfarm.ui.features.farmpreferences.data.local.entity.FarmEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FarmDao {
    @Query("SELECT * FROM farms WHERE userId = :userId ORDER BY isDefault DESC, createdAt DESC")
    fun getFarmsByUser(userId: String): Flow<List<FarmEntity>>

    @Query("SELECT * FROM farms WHERE id = :farmId AND userId = :userId")
    suspend fun getFarmById(farmId: String, userId: String): FarmEntity?

    @Query("SELECT * FROM farms WHERE isDefault = 1 AND userId = :userId LIMIT 1")
    suspend fun getDefaultFarm(userId: String): FarmEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFarm(farm: FarmEntity)

    @Update
    suspend fun updateFarm(farm: FarmEntity)

    @Query("UPDATE farms SET isDefault = 0 WHERE userId = :userId")
    suspend fun clearDefaultFarms(userId: String)

    @Query("UPDATE farms SET isDefault = 1 WHERE id = :farmId AND userId = :userId")
    suspend fun setDefaultFarm(farmId: String, userId: String)

    @Delete
    suspend fun deleteFarm(farm: FarmEntity)
}
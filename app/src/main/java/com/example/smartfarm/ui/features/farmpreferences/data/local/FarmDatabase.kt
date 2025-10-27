package com.example.smartfarm.ui.features.farmpreferences.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.smartfarm.ui.features.farmpreferences.data.local.dao.FarmDao
import com.example.smartfarm.ui.features.farmpreferences.data.local.entity.FarmEntity

@Database(
    entities = [FarmEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FarmDatabase : RoomDatabase() {
    abstract fun farmDao(): FarmDao
}
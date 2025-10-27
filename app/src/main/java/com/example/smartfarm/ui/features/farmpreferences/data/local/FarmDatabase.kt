package com.example.smartfarm.ui.features.farmpreferences.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.smartfarm.ui.features.farmpreferences.data.local.dao.FarmDao
import com.example.smartfarm.ui.features.farmpreferences.data.local.entity.FarmEntity
import com.example.smartfarm.ui.features.finance.data.converters.Converters
import com.example.smartfarm.ui.features.finance.data.local.dao.TransactionDao
import com.example.smartfarm.ui.features.finance.data.local.entity.TransactionEntity

@Database(
    entities = [
        FarmEntity::class,
    TransactionEntity::class
            ],
    version = 2,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class FarmDatabase : RoomDatabase() {
    abstract fun farmDao(): FarmDao
    abstract fun transactionDao(): TransactionDao
}

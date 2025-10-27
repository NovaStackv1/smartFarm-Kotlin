package com.example.smartfarm.di

import android.content.Context
import androidx.room.Room
import com.example.smartfarm.ui.features.farmpreferences.data.local.dao.FarmDao
import com.example.smartfarm.ui.features.farmpreferences.data.local.FarmDatabase
import com.example.smartfarm.ui.features.finance.data.local.dao.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFarmDatabase(@ApplicationContext context: Context): FarmDatabase {
        return Room.databaseBuilder(
            context,
            FarmDatabase::class.java,
            "farm_database"
        ).build()
    }

    @Provides
    fun provideFarmDao(database: FarmDatabase): FarmDao {
        return database.farmDao()
    }

    @Provides
    fun provideTransactionDao(database: FarmDatabase): TransactionDao {
        return database.transactionDao()
    }


}
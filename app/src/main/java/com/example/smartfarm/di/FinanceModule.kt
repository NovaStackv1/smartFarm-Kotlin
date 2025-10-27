package com.example.smartfarm.di

import com.example.smartfarm.ui.features.finance.data.local.dao.TransactionDao
import com.example.smartfarm.ui.features.finance.data.remote.FirestoreFinanceDataSource
import com.example.smartfarm.ui.features.finance.data.repository.FinanceRepositoryImpl
import com.example.smartfarm.ui.features.finance.domain.repository.FinanceRepository
import com.example.smartfarm.ui.features.finance.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FinanceModule {

    @Provides
    @Singleton
    fun provideFinanceRepository(
        transactionDao: TransactionDao,
        firestoreDataSource: FirestoreFinanceDataSource
    ): FinanceRepository {
        return FinanceRepositoryImpl(transactionDao, firestoreDataSource)
    }

    @Provides
    @Singleton
    fun provideGetTransactionsUseCase(repository: FinanceRepository): GetTransactionsUseCase {
        return GetTransactionsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAddTransactionUseCase(repository: FinanceRepository): AddTransactionUseCase {
        return AddTransactionUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteTransactionUseCase(repository: FinanceRepository): DeleteTransactionUseCase {
        return DeleteTransactionUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetFinancialSummaryUseCase(repository: FinanceRepository): GetFinancialSummaryUseCase {
        return GetFinancialSummaryUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSyncTransactionsUseCase(repository: FinanceRepository): SyncTransactionsUseCase {
        return SyncTransactionsUseCase(repository)
    }
}
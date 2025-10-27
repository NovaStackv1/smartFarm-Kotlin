package com.example.smartfarm.ui.features.finance.data.local.dao

import androidx.room.*
import com.example.smartfarm.ui.features.finance.data.local.entity.SyncStatus
import com.example.smartfarm.ui.features.finance.data.local.entity.TransactionEntity
import com.example.smartfarm.ui.features.finance.domain.model.TransactionCategory
import com.example.smartfarm.ui.features.finance.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TransactionDao {
    
    @Query("SELECT * FROM transactions WHERE farmId = :farmId AND userId = :userId ORDER BY date DESC")
    fun getTransactionsByFarm(userId: String, farmId: String): Flow<List<TransactionEntity>>
    
    @Query("SELECT * FROM transactions WHERE farmId = :farmId AND userId = :userId AND date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getTransactionsByDateRange(userId: String, farmId: String, startDate: LocalDate, endDate: LocalDate): Flow<List<TransactionEntity>>
    
    @Query("SELECT * FROM transactions WHERE farmId = :farmId AND userId = :userId AND category = :category ORDER BY date DESC")
    fun getTransactionsByCategory(userId: String, farmId: String, category: TransactionCategory): Flow<List<TransactionEntity>>
    
    @Query("SELECT * FROM transactions WHERE farmId = :farmId AND userId = :userId AND type = :type ORDER BY date DESC")
    fun getTransactionsByType(userId: String, farmId: String, type: TransactionType): Flow<List<TransactionEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)
    
    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)
    
    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)
    
    @Query("DELETE FROM transactions WHERE id = :transactionId AND userId = :userId")
    suspend fun deleteTransactionById(transactionId: String, userId: String)
    
    @Query("SELECT * FROM transactions WHERE syncStatus = 'PENDING' AND userId = :userId")
    suspend fun getPendingSyncTransactions(userId: String): List<TransactionEntity>
    
    @Query("UPDATE transactions SET syncStatus = :syncStatus WHERE id = :transactionId AND userId = :userId")
    suspend fun updateSyncStatus(transactionId: String, userId: String, syncStatus: SyncStatus)
}
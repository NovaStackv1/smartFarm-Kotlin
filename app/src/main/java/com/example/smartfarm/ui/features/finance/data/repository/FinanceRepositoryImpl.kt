package com.example.smartfarm.ui.features.finance.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.smartfarm.ui.features.finance.data.local.dao.TransactionDao
import com.example.smartfarm.ui.features.finance.data.local.entity.SyncStatus
import com.example.smartfarm.ui.features.finance.data.local.entity.TransactionEntity
import com.example.smartfarm.ui.features.finance.data.mapper.toDomain
import com.example.smartfarm.ui.features.finance.data.mapper.toEntity
import com.example.smartfarm.ui.features.finance.data.remote.FirestoreFinanceDataSource
import com.example.smartfarm.ui.features.finance.domain.model.FinancialSummary
import com.example.smartfarm.ui.features.finance.domain.model.Transaction
import com.example.smartfarm.ui.features.finance.domain.model.TransactionCategory
import com.example.smartfarm.ui.features.finance.domain.model.TransactionType
import com.example.smartfarm.ui.features.finance.domain.repository.FinanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class FinanceRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val firestoreDataSource: FirestoreFinanceDataSource
) : FinanceRepository {

    override fun getTransactions(userId: String, farmId: String): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByFarm(userId, farmId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTransactionsByDateRange(userId: String, farmId: String, startDate: LocalDate, endDate: LocalDate): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByDateRange(userId, farmId, startDate, endDate).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTransactionsByCategory(userId: String, farmId: String, category: TransactionCategory): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByCategory(userId, farmId, category).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addTransaction(userId: String, farmId: String, transaction: Transaction) {
        val entity = transaction.toEntity(userId, farmId)
        transactionDao.insertTransaction(entity)
        // Sync to Firestore in background
        firestoreDataSource.syncTransaction(userId, farmId, entity)
    }

    override suspend fun deleteTransaction(userId: String, transactionId: String) {
        // First get the transaction to know which farm it belongs to for Firestore deletion
        val transactions = getTransactions(userId, "").first() // Empty farmId to get all (you might need to adjust this)
        val transactionToDelete = transactions.find { it.id == transactionId }

        transactionDao.deleteTransactionById(transactionId, userId)

        // Sync deletion to Firestore
        transactionToDelete?.let { transaction ->
            firestoreDataSource.deleteTransaction(userId, transaction.farmId, transactionId)
        }
    }

//    override suspend fun deleteTransaction(userId: String, transactionId: String) {
//        transactionDao.deleteTransactionById(transactionId, userId)
//        firestoreDataSource.deleteTransaction(userId, transactionId)
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getFinancialSummary(userId: String, farmId: String, period: String): FinancialSummary {
        val transactions = getTransactions(userId, farmId).map { it }.first()
        
        val (startDate, endDate) = when (period) {
            "This Month" -> {
                val now = LocalDate.now()
                Pair(now.withDayOfMonth(1), now)
            }
            "This Year" -> {
                val now = LocalDate.now()
                Pair(now.withDayOfYear(1), now)
            }
            else -> Pair(LocalDate.MIN, LocalDate.MAX)
        }
        
        val filteredTransactions = transactions.filter { 
            it.date.isAfter(startDate.minusDays(1)) && it.date.isBefore(endDate.plusDays(1)) 
        }
        
        val income = filteredTransactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
        val expenses = filteredTransactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
        
        return FinancialSummary(
            farmId = farmId,
            totalIncome = income,
            totalExpenses = expenses,
            profit = income - expenses,
            period = period
        )
    }

    override suspend fun syncTransactions(userId: String) {
        val pendingTransactions = transactionDao.getPendingSyncTransactions(userId)
        pendingTransactions.forEach { transaction ->
            try {
                firestoreDataSource.syncTransaction(userId, transaction.farmId, transaction)
                transactionDao.updateSyncStatus(transaction.id, userId, SyncStatus.SYNCED)
            } catch (e: Exception) {
                transactionDao.updateSyncStatus(transaction.id, userId, SyncStatus.FAILED)
            }
        }
    }
}
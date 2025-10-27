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

//    override suspend fun deleteTransaction(userId: String, transactionId: String) {
//        // First get the transaction to know which farm it belongs to for Firestore deletion
//        val transactions = getTransactions(userId, "").first() // Empty farmId to get all (you might need to adjust this)
//        val transactionToDelete = transactions.find { it.id == transactionId }
//
//        transactionDao.deleteTransactionById(transactionId, userId)
//
//        // Sync deletion to Firestore
//        transactionToDelete?.let { transaction ->
//            firestoreDataSource.deleteTransaction(userId, transaction.farmId, transactionId)
//        }
//    }

    // In FinanceRepositoryImpl.kt - fix deleteTransaction properly
    override suspend fun deleteTransaction(userId: String, transactionId: String) {
        // Get the transaction first to know which farm it belongs to
        val transactionEntity = transactionDao.getTransactionById(transactionId, userId)

        // Delete from local database
        transactionDao.deleteTransactionById(transactionId, userId)

        // Sync deletion to Firestore if we found the transaction
        transactionEntity?.let { transaction ->
            firestoreDataSource.deleteTransaction(userId, transaction.farmId, transactionId)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getFinancialSummary(
        userId: String,
        farmId: String,
        period: String
    ): FinancialSummary {

        val transactions = getTransactions(userId, farmId).map { it }.first()

        val filteredTransactions = when (period) {
            "This Month" -> {
                val now = LocalDate.now()
                val startOfMonth = now.withDayOfMonth(1)
                transactions.filter {
                    it.date.isAfter(startOfMonth.minusDays(1)) && it.date.isBefore(now.plusDays(1))
                }
            }
            "This Year" -> {
                val now = LocalDate.now()
                val startOfYear = now.withDayOfYear(1)
                transactions.filter {
                    it.date.isAfter(startOfYear.minusDays(1)) && it.date.isBefore(now.plusDays(1))
                }
            }
            else -> {
                // For "All Time", use all transactions without date filtering
                transactions
            }
        }

        
        val income = filteredTransactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
        val expenses = filteredTransactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
        val profit = income - expenses

        return FinancialSummary(
            farmId = farmId,
            totalIncome = income,
            totalExpenses = expenses,
            profit = profit,
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
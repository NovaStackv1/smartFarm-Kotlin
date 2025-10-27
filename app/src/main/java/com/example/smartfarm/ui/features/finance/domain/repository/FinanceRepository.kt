package com.example.smartfarm.ui.features.finance.domain.repository

import com.example.smartfarm.ui.features.finance.domain.model.FinancialSummary
import com.example.smartfarm.ui.features.finance.domain.model.Transaction
import com.example.smartfarm.ui.features.finance.domain.model.TransactionCategory
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface FinanceRepository {
    fun getTransactions(userId: String, farmId: String): Flow<List<Transaction>>
    fun getTransactionsByDateRange(userId: String, farmId: String, startDate: LocalDate, endDate: LocalDate): Flow<List<Transaction>>
    fun getTransactionsByCategory(userId: String, farmId: String, category: TransactionCategory): Flow<List<Transaction>>
    suspend fun addTransaction(userId: String, farmId: String, transaction: Transaction)
    suspend fun deleteTransaction(userId: String, transactionId: String)
    suspend fun getFinancialSummary(userId: String, farmId: String, period: String): FinancialSummary
    suspend fun syncTransactions(userId: String)
}
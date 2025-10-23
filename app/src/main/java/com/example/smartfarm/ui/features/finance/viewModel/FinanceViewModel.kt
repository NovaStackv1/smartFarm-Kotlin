package com.example.smartfarm.ui.features.finance.viewModel


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfarm.ui.features.finance.model.FinancialSummary
import com.example.smartfarm.ui.features.finance.model.Transaction
import com.example.smartfarm.ui.features.finance.model.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class FinanceViewModel : ViewModel() {

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    private val _financialSummary = MutableStateFlow(FinancialSummary())
    val financialSummary: StateFlow<FinancialSummary> = _financialSummary.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadTransactions()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadTransactions() {
        viewModelScope.launch {
            _isLoading.value = true
            // TODO: Load from repository/database
            // Mock data for now
            _transactions.value = listOf(
                Transaction(
                    id = "1",
                    type = TransactionType.INCOME,
                    amount = 15000.0,
                    description = "Market Sale",
                    date = LocalDate.of(2024, 5, 20),
                    category = "Market Sale"
                ),
                Transaction(
                    id = "2",
                    type = TransactionType.EXPENSE,
                    amount = 2500.0,
                    description = "Seeds",
                    date = LocalDate.of(2024, 5, 18),
                    category = "Seeds"
                ),
                Transaction(
                    id = "3",
                    type = TransactionType.EXPENSE,
                    amount = 4000.0,
                    description = "Fertilizer",
                    date = LocalDate.of(2024, 5, 15),
                    category = "Fertilizer"
                ),
                Transaction(
                    id = "4",
                    type = TransactionType.INCOME,
                    amount = 30000.0,
                    description = "Livestock Sale",
                    date = LocalDate.of(2024, 5, 12),
                    category = "Livestock Sale"
                )
            )
            calculateSummary()
            _isLoading.value = false
        }
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            // TODO: Save to repository/database
            val updatedList = _transactions.value + transaction
            _transactions.value = updatedList.sortedByDescending { it.date }
            calculateSummary()
        }
    }

    fun deleteTransaction(transactionId: String) {
        viewModelScope.launch {
            // TODO: Delete from repository/database
            _transactions.value = _transactions.value.filter { it.id != transactionId }
            calculateSummary()
        }
    }

    private fun calculateSummary() {
        val income = _transactions.value
            .filter { it.type == TransactionType.INCOME }
            .sumOf { it.amount }

        val expenses = _transactions.value
            .filter { it.type == TransactionType.EXPENSE }
            .sumOf { it.amount }

        _financialSummary.value = FinancialSummary(
            totalIncome = income,
            totalExpenses = expenses,
            profit = income - expenses
        )
    }

    fun filterTransactionsByDateRange(startDate: LocalDate, endDate: LocalDate) {
        viewModelScope.launch {
            // TODO: Implement filtering logic
        }
    }
}
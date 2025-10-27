package com.example.smartfarm.ui.features.finance.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfarm.ui.features.finance.domain.model.FinancialSummary
import com.example.smartfarm.ui.features.finance.domain.model.Transaction
import com.example.smartfarm.ui.features.finance.domain.usecase.AddTransactionUseCase
import com.example.smartfarm.ui.features.finance.domain.usecase.DeleteTransactionUseCase
import com.example.smartfarm.ui.features.finance.domain.usecase.GetFinancialSummaryUseCase
import com.example.smartfarm.ui.features.finance.domain.usecase.GetTransactionsUseCase
import com.example.smartfarm.ui.features.finance.domain.usecase.SyncTransactionsUseCase
import com.example.smartfarm.ui.features.weather.domain.usecase.GetDefaultFarmUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinanceViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val addTransactionUseCase: AddTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val getFinancialSummaryUseCase: GetFinancialSummaryUseCase,
    private val getDefaultFarmUseCase: GetDefaultFarmUseCase,
    private val firebaseAuth: FirebaseAuth,
    private val syncTransactionsUseCase: SyncTransactionsUseCase, // Optional for manual sync
) : ViewModel() {

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    private val _financialSummary = MutableStateFlow(FinancialSummary())
    val financialSummary: StateFlow<FinancialSummary> = _financialSummary.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedFarmId = MutableStateFlow<String?>(null)
    val selectedFarmId: StateFlow<String?> = _selectedFarmId.asStateFlow()

    private val userId: String
        get() = firebaseAuth.currentUser?.uid ?: ""

    init {
        loadDefaultFarmAndTransactions()
    }

    private fun loadDefaultFarmAndTransactions() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val defaultFarm = getDefaultFarmUseCase(userId)
                if (defaultFarm != null) {
                    _selectedFarmId.value = defaultFarm.id
                    loadTransactionsForFarm(defaultFarm.id)
                    loadFinancialSummary(defaultFarm.id)
                } else {
                    // Handle case where no farms exist
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _isLoading.value = false
                // Handle error
            }
        }
    }

    fun selectFarm(farmId: String) {
        _selectedFarmId.value = farmId
        loadTransactionsForFarm(farmId)
        loadFinancialSummary(farmId)
    }

//    private fun loadTransactionsForFarm(farmId: String) {
//        viewModelScope.launch {
//            getTransactionsUseCase(userId, farmId).collect { transactionsList ->
//                _transactions.value = transactionsList
//            }
//        }
//    }
    // In FinanceViewModel.kt - add debug logging
    private fun loadTransactionsForFarm(farmId: String) {
        viewModelScope.launch {
            println("🔄 DEBUG: Starting to load transactions for farm: $farmId, user: $userId")
            getTransactionsUseCase(userId, farmId).collect { transactionsList ->
                println("✅ DEBUG: Successfully loaded ${transactionsList.size} transactions")
                transactionsList.forEach { transaction ->
                    println("   - ${transaction.type}: ${transaction.description} - KES ${transaction.amount}")
                }
                _transactions.value = transactionsList
            }
        }
    }


    // In FinanceViewModel.kt - add debug logging for financial summary
    private fun loadFinancialSummary(farmId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                println("💰 DEBUG: Calculating financial summary for farm: $farmId")
                val summary = getFinancialSummaryUseCase(userId, farmId, "All Time")
                println("💰 DEBUG: Summary calculated - Income: ${summary.totalIncome}, Expenses: ${summary.totalExpenses}, Profit: ${summary.profit}")
                _financialSummary.value = summary
            } catch (e: Exception) {
                println("❌ DEBUG: Error calculating financial summary: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            val farmId = _selectedFarmId.value ?: run {
                println("❌ DEBUG: Cannot add transaction - no farm selected")
                return@launch
            }
            println("➕ DEBUG: Adding transaction: ${transaction.description} for farm: $farmId")
            addTransactionUseCase(userId, farmId, transaction)
            println("✅ DEBUG: Transaction added successfully")
        }
    }

//    fun addTransaction(transaction: Transaction) {
//        viewModelScope.launch {
//            val farmId = _selectedFarmId.value ?: return@launch
//            addTransactionUseCase(userId, farmId, transaction)
//            // Transactions will update automatically via Flow
//        }
//    }


    fun deleteTransaction(transactionId: String) {
        viewModelScope.launch {
            deleteTransactionUseCase(userId, transactionId)
            // Transactions will update automatically via Flow
        }
    }

    fun refreshFinancialSummary(period: String) {
        viewModelScope.launch {
            val farmId = _selectedFarmId.value ?: return@launch
            val summary = getFinancialSummaryUseCase(userId, farmId, period)
            _financialSummary.value = summary
        }
    }

    // Optional: Manual sync method
    fun syncData() {
        viewModelScope.launch {
            syncTransactionsUseCase(userId)
        }
    }
}
package com.example.smartfarm.ui.features.finance.domain.usecase

import com.example.smartfarm.ui.features.finance.domain.model.Transaction
import com.example.smartfarm.ui.features.finance.domain.repository.FinanceRepository
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(userId: String, farmId: String, transaction: Transaction) {
        repository.addTransaction(userId, farmId, transaction)
    }
}
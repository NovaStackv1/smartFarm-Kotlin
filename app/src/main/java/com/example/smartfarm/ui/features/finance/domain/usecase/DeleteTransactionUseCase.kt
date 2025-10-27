package com.example.smartfarm.ui.features.finance.domain.usecase

import com.example.smartfarm.ui.features.finance.domain.repository.FinanceRepository
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(userId: String, transactionId: String) {
        repository.deleteTransaction(userId, transactionId)
    }
}
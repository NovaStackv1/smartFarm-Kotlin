package com.example.smartfarm.ui.features.finance.domain.usecase

import com.example.smartfarm.ui.features.finance.domain.model.Transaction
import com.example.smartfarm.ui.features.finance.domain.repository.FinanceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val repository: FinanceRepository
) {
    operator fun invoke(userId: String, farmId: String): Flow<List<Transaction>> {
        return repository.getTransactions(userId, farmId)
    }
}
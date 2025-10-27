package com.example.smartfarm.ui.features.finance.domain.usecase

import com.example.smartfarm.ui.features.finance.domain.model.FinancialSummary
import com.example.smartfarm.ui.features.finance.domain.repository.FinanceRepository
import javax.inject.Inject

class GetFinancialSummaryUseCase @Inject constructor(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(userId: String, farmId: String, period: String): FinancialSummary {
        return repository.getFinancialSummary(userId, farmId, period)
    }
}
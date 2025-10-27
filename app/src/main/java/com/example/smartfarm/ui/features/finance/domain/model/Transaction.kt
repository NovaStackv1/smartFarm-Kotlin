package com.example.smartfarm.ui.features.finance.domain.model

import java.time.LocalDate

data class Transaction(
    val id: String = "",
    val farmId: String = "", // Link to specific farm
    val type: TransactionType,
    val amount: Double,
    val description: String,
    val category: TransactionCategory,
    val date: LocalDate,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class TransactionType {
    INCOME,
    EXPENSE
}

enum class TransactionCategory {
    // Income Categories
    CROP_SALE,
    LIVESTOCK_SALE,
    DAIRY_SALE,
    EGG_SALE,
    GOVERNMENT_SUBSIDY,
    OTHER_INCOME,
    
    // Expense Categories
    SEEDS,
    FERTILIZER,
    PESTICIDES,
    LABOR,
    EQUIPMENT,
    FUEL,
    VETERINARY,
    FEED,
    FENCING,
    IRRIGATION,
    TRANSPORT,
    OTHER_EXPENSE
}

data class FinancialSummary(
    val farmId: String = "",
    val totalIncome: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val profit: Double = 0.0,
    val period: String = "All Time" // "This Month", "This Year", etc.
) {
    val balance: Double get() = profit


}
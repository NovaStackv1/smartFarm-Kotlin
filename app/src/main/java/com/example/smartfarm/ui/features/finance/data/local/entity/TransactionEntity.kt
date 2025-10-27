package com.example.smartfarm.ui.features.finance.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.smartfarm.ui.features.finance.domain.model.TransactionCategory
import com.example.smartfarm.ui.features.finance.domain.model.TransactionType
import java.time.LocalDate

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey
    val id: String,
    val farmId: String,
    val type: TransactionType,
    val amount: Double,
    val description: String,
    val category: TransactionCategory,
    val date: LocalDate,
    val createdAt: Long,
    val updatedAt: Long,
    val userId: String, // For Firestore sync
    val syncStatus: SyncStatus = SyncStatus.PENDING
)

enum class SyncStatus {
    SYNCED,
    PENDING,
    FAILED
}
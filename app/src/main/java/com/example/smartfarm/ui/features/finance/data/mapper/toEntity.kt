package com.example.smartfarm.ui.features.finance.data.mapper

import com.example.smartfarm.ui.features.finance.data.local.entity.TransactionEntity
import com.example.smartfarm.ui.features.finance.data.local.entity.SyncStatus
import com.example.smartfarm.ui.features.finance.domain.model.Transaction
import com.example.smartfarm.ui.features.finance.domain.model.TransactionCategory
import com.example.smartfarm.ui.features.finance.domain.model.TransactionType
import java.time.LocalDate

fun Transaction.toEntity(userId: String, farmId: String): TransactionEntity {
    return TransactionEntity(
        id = id,
        farmId = farmId,
        type = type,
        amount = amount,
        description = description,
        category = category,
        date = date,
        createdAt = createdAt,
        updatedAt = updatedAt,
        userId = userId,
        syncStatus = SyncStatus.PENDING
    )
}

fun TransactionEntity.toDomain(): Transaction {
    return Transaction(
        id = id,
        farmId = farmId,
        type = type,
        amount = amount,
        description = description,
        category = category,
        date = date,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
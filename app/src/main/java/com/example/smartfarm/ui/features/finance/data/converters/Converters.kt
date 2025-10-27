package com.example.smartfarm.ui.features.finance.data.converters

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.example.smartfarm.ui.features.finance.data.local.entity.SyncStatus
import com.example.smartfarm.ui.features.finance.domain.model.TransactionCategory
import com.example.smartfarm.ui.features.finance.domain.model.TransactionType
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Converters {
    
    // LocalDate converters
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE) }
    }

    // TransactionType converters
    @TypeConverter
    fun fromTransactionType(type: TransactionType?): String? {
        return type?.name
    }

    @TypeConverter
    fun toTransactionType(typeString: String?): TransactionType? {
        return typeString?.let { TransactionType.valueOf(it) }
    }

    // TransactionCategory converters
    @TypeConverter
    fun fromTransactionCategory(category: TransactionCategory?): String? {
        return category?.name
    }

    @TypeConverter
    fun toTransactionCategory(categoryString: String?): TransactionCategory? {
        return categoryString?.let { TransactionCategory.valueOf(it) }
    }

    // SyncStatus converters
    @TypeConverter
    fun fromSyncStatus(status: SyncStatus?): String? {
        return status?.name
    }

    @TypeConverter
    fun toSyncStatus(statusString: String?): SyncStatus? {
        return statusString?.let { SyncStatus.valueOf(it) }
    }
}
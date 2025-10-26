package com.example.smartfarm.ui.features.farmpreferences.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "farms")
data class FarmEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val locationName: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val size: Double,
    val cropTypes: String, // JSON string of List<CropType>
    val soilType: String,
    val irrigationMethod: String,
    val createdAt: Long,
    val isDefault: Boolean,
    val userId: String // Link to Firebase user
)


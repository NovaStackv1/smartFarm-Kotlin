// FarmMappers.kt
package com.example.smartfarm.ui.features.farmpreferences.data.mapper

import com.example.smartfarm.ui.features.farmpreferences.data.local.entity.FarmEntity
import com.example.smartfarm.ui.features.farmpreferences.domain.models.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun Farm.toEntity(userId: String): FarmEntity {
    return FarmEntity(
        id = id,
        name = name,
        locationName = location.name,
        latitude = location.latitude,
        longitude = location.longitude,
        address = location.address,
        size = size,
        cropTypes = Gson().toJson(cropTypes),
        soilType = soilType.name,
        irrigationMethod = irrigationMethod.name,
        createdAt = createdAt,
        isDefault = isDefault,
        userId = userId
    )
}

fun FarmEntity.toDomain(): Farm {
    val cropTypeList = try {
        val listType = object : TypeToken<List<CropType>>() {}.type
        Gson().fromJson<List<CropType>>(cropTypes, listType) ?: emptyList()
    } catch (e: Exception) {
        emptyList<CropType>()
    }
    
    return Farm(
        id = id,
        name = name,
        location = FarmLocation(
            name = locationName,
            latitude = latitude,
            longitude = longitude,
            address = address
        ),
        size = size,
        cropTypes = cropTypeList,
        soilType = SoilType.valueOf(soilType),
        irrigationMethod = IrrigationMethod.valueOf(irrigationMethod),
        createdAt = createdAt,
        isDefault = isDefault
    )
}
package com.example.smartfarm.ui.features.farmpreferences.domain.models

//import android.os.Parcelable
//import kotlinx.parcelize.Parcelize
import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Farm(
    val id: String = "",
    val name: String = "",
    val location: FarmLocation,
    val size: Double = 0.0, // in acres/hectares
    val cropTypes: List<CropType> = emptyList(),
    val soilType: SoilType = SoilType.LOAM,
    val irrigationMethod: IrrigationMethod = IrrigationMethod.DRIP,
    val createdAt: Long = System.currentTimeMillis(),
    val isDefault: Boolean = false
)

@Parcelize
data class FarmLocation(
    val name: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val address: String = ""
) : Parcelable


enum class CropType {
    MAIZE, BEANS, WHEAT, COFFEE, TEA, VEGETABLES, FRUITS, OTHER
}

enum class SoilType {
    SANDY, CLAY, LOAM, SILT, PEAT, CHALK
}

enum class IrrigationMethod {
    DRIP, SPRINKLER, FURROW, FLOOD, MANUAL
}
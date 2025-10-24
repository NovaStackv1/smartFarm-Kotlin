package com.example.smartfarm.ui.features.weather.domain.usecase

import android.annotation.SuppressLint
import android.content.Context
import com.example.smartfarm.ui.features.weather.domain.models.LocationData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class GetCurrentLocationUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient
) {
    @SuppressLint("MissingPermission")
    suspend operator fun invoke(): Result<LocationData> = suspendCancellableCoroutine { continuation ->
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
            .addOnSuccessListener { location ->
                if (location != null) {
                    continuation.resume(
                        Result.success(
                            LocationData(
                                name = "Current Location",
                                lat = location.latitude,
                                lon = location.longitude
                            )
                        )
                    )
                } else {
                    continuation.resume(
                        Result.failure(Exception("Unable to get current location"))
                    )
                }
            }
            .addOnFailureListener { exception ->
                continuation.resume(Result.failure(exception))
            }
    }
}
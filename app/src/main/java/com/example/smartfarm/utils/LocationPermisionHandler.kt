package com.example.smartfarm.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.smartfarm.ui.features.farmpreferences.domain.models.FarmLocation

@Composable
fun rememberLocationPermissionState(
    onPermissionGranted: () -> Unit = {},
    onPermissionDenied: () -> Unit = {}
): LocationPermissionState {
    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(checkLocationPermission(context))
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.values.all { it }
        hasPermission = granted

        if (granted) {
            onPermissionGranted()
        } else {
            onPermissionDenied()
        }
    }

    return remember {
        LocationPermissionState(
            hasPermission = hasPermission,
            requestPermission = {
                launcher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        )
    }
}

data class LocationPermissionState(
    val hasPermission: Boolean,
    val requestPermission: () -> Unit
)

fun checkLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
}

// Map Location Picker Utility
@Composable
fun rememberMapLocationState(): MapLocationState {
    var selectedLocation by remember { mutableStateOf<FarmLocation?>(null) }
    var isMapVisible by remember { mutableStateOf(false) }

    return remember {
        MapLocationState(
            selectedLocation = selectedLocation,
            isMapVisible = isMapVisible,
            onLocationSelected = { location ->
                selectedLocation = location
                isMapVisible = false
            },
            showMap = { isMapVisible = true },
            hideMap = { isMapVisible = false },
            clearLocation = { selectedLocation = null }
        )
    }
}

data class MapLocationState(
    val selectedLocation: FarmLocation?,
    val isMapVisible: Boolean,
    val onLocationSelected: (FarmLocation) -> Unit,
    val showMap: () -> Unit,
    val hideMap: () -> Unit,
    val clearLocation: () -> Unit
)
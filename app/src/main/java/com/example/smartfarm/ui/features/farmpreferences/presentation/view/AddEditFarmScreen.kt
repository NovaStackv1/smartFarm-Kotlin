package com.example.smartfarm.ui.features.farmpreferences.presentation.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.smartfarm.ui.features.farmpreferences.domain.models.FarmLocation
import com.example.smartfarm.ui.features.farmpreferences.presentation.components.FarmForm
import com.example.smartfarm.ui.features.farmpreferences.presentation.viewModel.AddEditFarmViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditFarmScreen(
    farmId: String? = null,
    onNavigateBack: () -> Unit,
    navController: NavController,
    onNavigateToMap: (FarmLocation?) -> Unit,
    addEditFarmViewModel: AddEditFarmViewModel = hiltViewModel()
) {
    val uiState by addEditFarmViewModel.uiState.collectAsState()
    //val mapLocationState = rememberMapLocationState()

    // Listen for location selection from map
    val selectedLocation by navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<FarmLocation?>("selectedLocation", null)
        ?.collectAsState() ?: remember { mutableStateOf(null) }


    // Load farm data if editing
    LaunchedEffect(farmId) {
        if (farmId != null) {
            addEditFarmViewModel.loadFarm(farmId)
        }
    }

    // Handle map location selection
    LaunchedEffect(selectedLocation) {
        println("DEBUG: selectedLocation changed: $selectedLocation")
        selectedLocation?.let { location ->
            println("DEBUG: Updating location in ViewModel: $location")
            addEditFarmViewModel.updateLocation(location)
            // Clear the saved location to avoid reprocessing
            navController.currentBackStackEntry?.savedStateHandle?.remove<FarmLocation>("selectedLocation")
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (farmId == null) "Add Farm" else "Edit Farm",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { 
                            if (addEditFarmViewModel.validateForm()) {
                                addEditFarmViewModel.saveFarm()
                                onNavigateBack()
                            }
                        }
                    ) {
                        Text("Save")
                    }
                }
            )
        }
    ) { paddingValues ->
        FarmForm(
            uiState = uiState,
            onFarmNameChange = { addEditFarmViewModel.updateFarmName(it) },
            onSizeChange = { addEditFarmViewModel.updateSize(it) },
            onCropTypesChange = { addEditFarmViewModel.updateCropTypes(it) },
            onSoilTypeChange = { addEditFarmViewModel.updateSoilType(it) },
            onIrrigationChange = { addEditFarmViewModel.updateIrrigationMethod(it) },
            onLocationClick = { onNavigateToMap(uiState.farm.location) },
            onSetDefaultChange = { addEditFarmViewModel.updateIsDefault(it) },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        )
    }
}

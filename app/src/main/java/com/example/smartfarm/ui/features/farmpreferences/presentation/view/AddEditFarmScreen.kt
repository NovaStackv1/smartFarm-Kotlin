package com.example.smartfarm.ui.features.farmpreferences.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartfarm.ui.features.farmpreferences.domain.models.*
import com.example.smartfarm.utils.rememberMapLocationState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditFarmScreen(
    farmId: String? = null,
    onNavigateBack: () -> Unit,
    onNavigateToMap: (FarmLocation?) -> Unit,
    addEditFarmViewModel: AddEditFarmViewModel = hiltViewModel()
) {
    val uiState by addEditFarmViewModel.uiState.collectAsState()
    val mapLocationState = rememberMapLocationState()

    // Load farm data if editing
    LaunchedEffect(farmId) {
        if (farmId != null) {
            addEditFarmViewModel.loadFarm(farmId)
        }
    }

    // Handle map location selection
    LaunchedEffect(mapLocationState.selectedLocation) {
        mapLocationState.selectedLocation?.let { location ->
            addEditFarmViewModel.updateLocation(location)
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

@Composable
fun FarmForm(
    uiState: AddEditFarmViewModel.AddEditFarmUiState,
    onFarmNameChange: (String) -> Unit,
    onSizeChange: (Double) -> Unit,
    onCropTypesChange: (List<CropType>) -> Unit,
    onSoilTypeChange: (SoilType) -> Unit,
    onIrrigationChange: (IrrigationMethod) -> Unit,
    onLocationClick: () -> Unit,
    onSetDefaultChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Farm Name
        OutlinedTextField(
            value = uiState.farm.name,
            onValueChange = onFarmNameChange,
            label = { Text("Farm Name") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.errors.contains(AddEditFarmViewModel.FormError.NAME_EMPTY)
        )

        // Location
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onLocationClick() },
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Farm Location",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = uiState.farm.location.name.ifEmpty { "Tap to select location on map" },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Farm Size
        OutlinedTextField(
            value = uiState.farm.size.toString(),
            onValueChange = { 
                val size = it.toDoubleOrNull() ?: 0.0
                onSizeChange(size)
            },
            label = { Text("Farm Size (acres)") },
            modifier = Modifier.fillMaxWidth()
        )

        // Crop Types (Multi-select)
        Text("Crop Types", style = MaterialTheme.typography.bodyLarge)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CropType.entries.forEach { cropType ->
                val isSelected = uiState.farm.cropTypes.contains(cropType)
                FilterChip(
                    selected = isSelected,
                    onClick = { 
                        val newSelection = if (isSelected) {
                            uiState.farm.cropTypes - cropType
                        } else {
                            uiState.farm.cropTypes + cropType
                        }
                        onCropTypesChange(newSelection)
                    },
                    label = { Text(cropType.name) }
                )
            }
        }

        // Soil Type
        Text("Soil Type", style = MaterialTheme.typography.bodyLarge)
        Column {
            SoilType.entries.forEach { soilType ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSoilTypeChange(soilType) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = uiState.farm.soilType == soilType,
                        onClick = { onSoilTypeChange(soilType) }
                    )
                    Text(soilType.name)
                }
            }
        }

        // Irrigation Method
        Text("Irrigation Method", style = MaterialTheme.typography.bodyLarge)
        Column {
            IrrigationMethod.entries.forEach { method ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onIrrigationChange(method) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = uiState.farm.irrigationMethod == method,
                        onClick = { onIrrigationChange(method) }
                    )
                    Text(method.name)
                }
            }
        }

        // Set as Default
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = uiState.farm.isDefault,
                onCheckedChange = onSetDefaultChange
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Set as default farm for weather updates")
        }
    }
}
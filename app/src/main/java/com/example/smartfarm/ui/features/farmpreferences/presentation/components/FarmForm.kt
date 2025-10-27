package com.example.smartfarm.ui.features.farmpreferences.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartfarm.ui.features.farmpreferences.domain.models.CropType
import com.example.smartfarm.ui.features.farmpreferences.domain.models.IrrigationMethod
import com.example.smartfarm.ui.features.farmpreferences.domain.models.SoilType
import com.example.smartfarm.ui.features.farmpreferences.presentation.viewModel.AddEditFarmUiState
import com.example.smartfarm.ui.features.farmpreferences.presentation.viewModel.FormError

@Composable
fun FarmForm(
    uiState: AddEditFarmUiState,
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
            isError = uiState.errors.contains(FormError.NAME_EMPTY)
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
                    Text(
                        soilType.name,
                        style = MaterialTheme.typography.bodyMedium
                    )
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
                    Text(
                        method.name,
                        style = MaterialTheme.typography.bodyMedium
                    )
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
            Text(
                "Set as default farm for weather updates",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
package com.example.smartfarm.ui.features.finance.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Business
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartfarm.ui.features.farmpreferences.domain.models.Farm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmSelectionDropdown(
    farms: List<Farm>,
    selectedFarm: Farm?,
    onFarmSelected: (Farm) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedFarm?.name ?: "Select Farm",
            onValueChange = {},
            readOnly = true,
            leadingIcon = {
                Icon(Icons.Default.Business, "Farm")
            },
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, null)
            },
            colors = OutlinedTextFieldDefaults.colors(),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (farms.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("No farms available") },
                    onClick = { expanded = false }
                )
            } else {
                farms.forEach { farm ->
                    DropdownMenuItem(
                        text = { 
                            Column {
                                Text(farm.name, style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    farm.location.name, 
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        onClick = {
                            onFarmSelected(farm)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
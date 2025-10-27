package com.example.smartfarm.ui.features.weather.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.smartfarm.ui.features.farmpreferences.domain.models.Farm

@Composable
fun FarmLocationSelector(
    farms: List<Farm>,
    currentFarm: Farm?,
    onFarmSelected: (Farm) -> Unit,
    onManageFarms: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Select Farm Location"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            // Current Location Option
            DropdownMenuItem(
                text = { Text("Current Location") },
                onClick = {
                    // Handle current location selection
                    expanded = false
                }
            )

            HorizontalDivider(
                Modifier,
                DividerDefaults.Thickness,
                DividerDefaults.color
            )
            // Farm Locations
            farms.forEach { farm ->
                DropdownMenuItem(
                    text = { 
                        Column {
                            Text(farm.name)
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

            HorizontalDivider(
                Modifier,
                DividerDefaults.Thickness,
                DividerDefaults.color
            )
            
            DropdownMenuItem(
                text = { Text("Manage Farms") },
                onClick = {
                    onManageFarms()
                    expanded = false
                }
            )
        }
    }
}
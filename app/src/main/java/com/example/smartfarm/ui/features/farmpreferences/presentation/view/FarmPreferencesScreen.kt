package com.example.smartfarm.ui.features.farmpreferences.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.smartfarm.ui.features.farmpreferences.domain.models.Farm
import com.example.smartfarm.ui.features.farmpreferences.presentation.viewModel.FarmPreferencesUiState
import com.example.smartfarm.ui.features.farmpreferences.presentation.viewModel.FarmPreferencesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmPreferencesScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAddFarm: () -> Unit,
    onNavigateToEditFarm: (String) -> Unit,
    farmPreferencesViewModel: FarmPreferencesViewModel = hiltViewModel()
) {
    val farms by farmPreferencesViewModel.farms.collectAsState()
    val uiState by farmPreferencesViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Farm Preferences",
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
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddFarm,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, "Add Farm")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (farms.isEmpty()) {
                EmptyFarmsState(onAddFarm = onNavigateToAddFarm)
            } else {
                FarmsList(
                    farms = farms,
                    onSetDefault = { farmId -> farmPreferencesViewModel.setDefaultFarm(farmId) },
                    onEditFarm = onNavigateToEditFarm,
                    onDeleteFarm = { farmId -> farmPreferencesViewModel.deleteFarm(farmId) }
                )
            }
        }

        // Handle errors
        if (uiState is FarmPreferencesUiState.Error) {
            val errorMessage = (uiState as FarmPreferencesUiState.Error).message
            LaunchedEffect(errorMessage) {
                // Show snackbar or dialog
            }
        }
    }
}

@Composable
fun EmptyFarmsState(onAddFarm: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No Farms Added",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Add your first farm to get personalized weather updates and farming advice",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )
        Button(onClick = onAddFarm) {
            Text("Add Your First Farm")
        }
    }
}

@Composable
fun FarmsList(
    farms: List<Farm>,
    onSetDefault: (String) -> Unit,
    onEditFarm: (String) -> Unit,
    onDeleteFarm: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(farms) { farm ->
            FarmCard(
                farm = farm,
                onSetDefault = onSetDefault,
                onEdit = onEditFarm,
                onDelete = onDeleteFarm
            )
        }
    }
}

@Composable
fun FarmCard(
    farm: Farm,
    onSetDefault: (String) -> Unit,
    onEdit: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = farm.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                IconButton(
                    onClick = { onSetDefault(farm.id) },
                    enabled = !farm.isDefault
                ) {
                    Icon(
                        imageVector = if (farm.isDefault) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = if (farm.isDefault) "Default Farm" else "Set as Default",
                        tint = if (farm.isDefault) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = farm.location.name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "${farm.size} acres • ${farm.cropTypes.joinToString { it.name }}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { onEdit(farm.id) }) {
                    Text("Edit")
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = { showDeleteDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Farm",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Farm") },
            text = { Text("Are you sure you want to delete ${farm.name}? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete(farm.id)
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
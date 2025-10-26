package com.example.smartfarm.ui.features.farmpreferences.presentation.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.smartfarm.ui.features.farmpreferences.presentation.components.EmptyFarmsState
import com.example.smartfarm.ui.features.farmpreferences.presentation.components.FarmsList
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
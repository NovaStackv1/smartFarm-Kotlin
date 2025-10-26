package com.example.smartfarm.ui.features.farmpreferences.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartfarm.ui.features.farmpreferences.domain.models.Farm

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
package com.example.smartfarm.ui.features.finance.presentation.view


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartfarm.ui.features.farmpreferences.presentation.viewModel.FarmPreferencesViewModel
import com.example.smartfarm.ui.features.finance.presentation.components.AddTransactionDialog
import com.example.smartfarm.ui.features.finance.presentation.components.FarmSelectionDropdown
import com.example.smartfarm.ui.features.finance.presentation.components.FinancialSummaryCard
import com.example.smartfarm.ui.features.finance.presentation.components.TransactionItem
import com.example.smartfarm.ui.features.finance.presentation.viewModel.FinanceViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceScreen(
    financeViewModel: FinanceViewModel = hiltViewModel()
) {
    val transactions by financeViewModel.transactions.collectAsState()
    val summary by financeViewModel.financialSummary.collectAsState()
    val isLoading by financeViewModel.isLoading.collectAsState()
    val selectedFarmId by financeViewModel.selectedFarmId.collectAsState()

    // Get farms from farm preferences (you'll need to inject this)
    val farmPreferencesViewModel: FarmPreferencesViewModel = hiltViewModel()
    val farms by farmPreferencesViewModel.farms.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }

    val selectedFarm = farms.find { it.id == selectedFarmId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                    ) {
                        Text(
                            text = "Farm Finances",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.height(56.dp)
            )

            // Farm Selection
            if (farms.isNotEmpty()) {
                FarmSelectionDropdown(
                    farms = farms,
                    selectedFarm = selectedFarm,
                    onFarmSelected = { farm ->
                        financeViewModel.selectFarm(farm.id)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }


        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Transaction"
                )
            }
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (selectedFarm == null) {
            // No farm selected state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Business,
                        contentDescription = "No Farm",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "Select a Farm to View Finances",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "Choose a farm from the dropdown above to start tracking finances",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else if (transactions.isEmpty()) {
            // No transactions state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = "No Transactions",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "No Transactions Yet",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "Add your first transaction to start tracking farm finances",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    Button(onClick = { showAddDialog = true }) {
                        Text("Add First Transaction")
                    }
                }
            }
        } else {
            // Normal state with transactions
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Financial Summary Card
                item {
                    FinancialSummaryCard(
                        summary = summary,
                        farmName = selectedFarm.name
                    )
                }

                // Transactions Header
                item {
                    Text(
                        "Recent Transactions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Transactions List
                items(transactions) { transaction ->
                    TransactionItem(
                        transaction = transaction,
                        onDelete = { financeViewModel.deleteTransaction(transaction.id) }
                    )
                }

                // Bottom spacer for FAB
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }

    if (showAddDialog && selectedFarm != null) {
        AddTransactionDialog(
            farmName = selectedFarm.name,
            onDismiss = { showAddDialog = false },
            onSave = { transaction ->
                financeViewModel.addTransaction(transaction)
                showAddDialog = false
            }
        )
    }

}
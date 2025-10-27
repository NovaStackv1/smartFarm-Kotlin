package com.example.smartfarm.ui.features.finance.presentation.components


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.smartfarm.ui.features.finance.domain.model.Transaction
import com.example.smartfarm.ui.features.finance.domain.model.TransactionCategory
import com.example.smartfarm.ui.features.finance.domain.model.TransactionType
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDialog(
    farmName: String,
    onDismiss: () -> Unit,
    onSave: (Transaction) -> Unit
) {
    var transactionType by remember { mutableStateOf(TransactionType.EXPENSE) }
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var categoryExpanded by remember { mutableStateOf(false) }


    // Create proper category mapping
    val categoryMapping = mapOf(
        // Display Name to Enum Value
        "Seeds" to TransactionCategory.SEEDS,
        "Fertilizer" to TransactionCategory.FERTILIZER,
        "Pesticides" to TransactionCategory.PESTICIDES,
        "Tools" to TransactionCategory.EQUIPMENT,
        "Labor" to TransactionCategory.LABOR,
        "Market Sale" to TransactionCategory.CROP_SALE,
        "Livestock Sale" to TransactionCategory.LIVESTOCK_SALE,
        "Crop Sale" to TransactionCategory.CROP_SALE,
        "Other" to TransactionCategory.OTHER_EXPENSE
    )

    // Update categories based on transaction type
    val categories = remember(transactionType) {
        when (transactionType) {
            TransactionType.INCOME -> listOf(
                "Crop Sale",
                "Livestock Sale",
                "Dairy Sale",
                "Egg Sale",
                "Government Subsidy",
                "Other Income"
            )
            TransactionType.EXPENSE -> listOf(
                "Seeds",
                "Fertilizer",
                "Pesticides",
                "Labor",
                "Equipment",
                "Fuel",
                "Veterinary",
                "Feed",
                "Fencing",
                "Irrigation",
                "Transport",
                "Other Expense"
            )
        }
    }
    var selectedCategory by remember { mutableStateOf(categories[0]) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = "Add Transaction",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "for $farmName",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Transaction Type Selector
                Text(
                    text = "Type",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = if (transactionType == TransactionType.INCOME) "Income" else "Expense",
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Income") },
                            onClick = {
                                transactionType = TransactionType.INCOME
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Expense") },
                            onClick = {
                                transactionType = TransactionType.EXPENSE
                                expanded = false
                            }
                        )
                    }
                }

                // Category Selector
                Text(
                    text = "Category",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors()
                    )

                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = {
                                    selectedCategory = category
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                // Amount Input
                Text(
                    text = "Amount",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("KSh 0.00") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    prefix = { Text("KSh ") },
                    singleLine = true
                )

                // Description Input
                Text(
                    text = "Description",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g., Purchase of seeds") },
                    minLines = 1,
                    maxLines = 3
                )

                // Date Selector
                Text(
                    text = "Date",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = selectedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Select Date"
                            )
                        }
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (amount.isNotBlank() && description.isNotBlank()) {
                        // FIX: Use the mapping instead of direct valueOf
                        val categoryEnum = categoryMapping[selectedCategory] ?:
                        if (transactionType == TransactionType.INCOME)
                            TransactionCategory.OTHER_INCOME
                        else
                            TransactionCategory.OTHER_EXPENSE

                        val transaction = Transaction(
                            id = UUID.randomUUID().toString(),
                            type = transactionType,
                            amount = amount.toDoubleOrNull() ?: 0.0,
                            description = description,
                            date = selectedDate,
                            category = categoryEnum
                        )
                        onSave(transaction)
                    }
                },
                enabled = amount.isNotBlank() && description.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(
                state = rememberDatePickerState(
                    initialSelectedDateMillis = System.currentTimeMillis()
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
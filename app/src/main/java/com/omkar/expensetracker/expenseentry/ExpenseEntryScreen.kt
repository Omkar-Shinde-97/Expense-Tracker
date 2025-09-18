package com.omkar.expensetracker.expenseentry

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.omkar.expensetracker.R
import com.omkar.expensetracker.database.entity.ExpenseEntity
import com.omkar.expensetracker.utils.AppConstants.FOOD
import com.omkar.expensetracker.utils.AppConstants.STAFF
import com.omkar.expensetracker.utils.AppConstants.TRAVEL
import com.omkar.expensetracker.utils.AppConstants.UTILITY
import com.omkar.expensetracker.utils.showToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseEntryScreen(
    navController: NavController,
    viewModel: ExpenseEntryViewmodel = hiltViewModel(),
) {
    val totalSpentToday by viewModel.totalSpentToday
    val context = LocalContext.current
    ExpenseEntryScreenComponent(
        totalSpentToday = totalSpentToday, onSubmit = { title, amount, category, notes, receipt ->
            viewModel.addExpense(
                ExpenseEntity(
                    title = title,
                    amount = amount,
                    category = category,
                    notes = notes,
                    receipt = receipt
                )
            )
            context.showToast(R.string.expense_saved_successfully)
            navController.popBackStack()
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseEntryScreenComponent(
    totalSpentToday: Double?,
    onSubmit: (title: String, amount: Double, category: String, notes: String?, receipt: String?) -> Unit,
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(STAFF) }
    var notes by remember { mutableStateOf("") }
    var receipt by remember { mutableStateOf<String?>(null) }

    val categories = listOf(STAFF, TRAVEL, FOOD, UTILITY)
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Total Spent Today: ₹$totalSpentToday",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(stringResource(R.string.expense_title_label)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { if (it.all { c -> c.isDigit() }) amount = it },
            label = { Text(stringResource(R.string.expense_amount_label)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        ExposedDropdownMenuBox(
            expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                value = category,
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.select_category)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded, onDismissRequest = { expanded = false }) {
                categories.forEach { option ->
                    DropdownMenuItem(text = { Text(option) }, onClick = {
                        category = option
                        expanded = false
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = notes,
            onValueChange = { if (it.length <= 100) notes = it },
            label = { Text(stringResource(R.string.expense_notes_label)) },
            singleLine = false,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clickable { receipt = "mock_receipt.jpg" }, contentAlignment = Alignment.Center
        ) {
            if (receipt != null) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = stringResource(R.string.tap_to_add_receipt)
                )
            } else {
                Text(
                    stringResource(R.string.tap_to_add_receipt),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (title.isNotBlank() && amount.isNotBlank()) {
                    if (amount.toDouble() > 0) onSubmit(
                        title, amount.toDouble(), category, notes, receipt
                    )
                    else context.showToast(R.string.amount_should_greater_than_zero)
                } else {
                    if (title.isBlank()) context.showToast(R.string.error_empty_title)
                    else context.showToast(
                        R.string.error_invalid_amount
                    )
                }
            }, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.submit))
        }
    }
}


@PreviewLightDark
@Composable
private fun ExpenseEntryScreenPreview() {
    ExpenseEntryScreenComponent(
        totalSpentToday = 1000.0, onSubmit = { title, amount, category, notes, receipt -> })
}

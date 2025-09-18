package com.omkar.expensetracker.expenselist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.omkar.expensetracker.R
import com.omkar.expensetracker.database.entity.ExpenseEntity
import com.omkar.expensetracker.ui.theme.ExpenseTrackerTheme
import com.omkar.expensetracker.utils.toFormattedDate
import com.omkar.expensetracker.utils.toFormattedDateString
import java.time.Instant

enum class GroupingMode {
    BY_CATEGORY, BY_DATE
}

sealed interface ExpenseDisplayItem {
    data class Header(val title: String) : ExpenseDisplayItem
    data class ExpenseItem(val expense: ExpenseEntity) : ExpenseDisplayItem
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(
    expenses: List<ExpenseEntity>,
    selectedDate: String?,
    totalAmount: Double,
    onDateChange: (String?) -> Unit,
    displayedItems: List<ExpenseDisplayItem>,
    currentGroupingMode: GroupingMode,
    onToggleGroupingMode: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        var showDatePickerDialog by remember { mutableStateOf(false) }
        val initialSelectedDateMillis = Instant.now().toEpochMilli()
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialSelectedDateMillis
        )
        val confirmEnabled by remember {
            derivedStateOf { datePickerState.selectedDateMillis != null }
        }
        if (showDatePickerDialog) {
            DatePickerDialog(onDismissRequest = {
                showDatePickerDialog = false
            }, confirmButton = {
                TextButton(
                    onClick = {
                        showDatePickerDialog = false
                        onDateChange(
                            datePickerState.selectedDateMillis?.toFormattedDateString() ?: ""
                        )
                    }, enabled = confirmEnabled
                ) {
                    Text(stringResource(R.string.ok))
                }
            }, dismissButton = {
                TextButton(
                    onClick = {
                        showDatePickerDialog = false
                    }) {
                    Text(stringResource(R.string.cancel))
                }
            }) {
                DatePicker(state = datePickerState)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = { showDatePickerDialog = true }) {
                Icon(
                    Icons.Default.DateRange, contentDescription = stringResource(R.string.calendar)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = selectedDate ?: stringResource(R.string.select_date))
                Icon(
                    Icons.Default.Clear,
                    contentDescription = stringResource(R.string.clear_date_filter),
                    modifier = Modifier
                        .padding(2.dp)
                        .clickable {
                            onDateChange(
                                null
                            )
                        })
            }

            TextButton(onClick = { onToggleGroupingMode() }) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Toggle Group")
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (currentGroupingMode == GroupingMode.BY_CATEGORY) stringResource(R.string.group_by_category) else stringResource(
                        R.string.group_by_time
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Total: ₹$totalAmount | Count: ${expenses.size}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (expenses.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_expenses_found),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                displayedItems.forEach { item ->
                    when (item) {
                        is ExpenseDisplayItem.Header -> {
                            stickyHeader { GroupHeader(title = item.title) }
                        }

                        is ExpenseDisplayItem.ExpenseItem -> {
                            item { ExpenseCard(expense = item.expense) }
                        }
                    }
                }


            }
        }
    }
}

@Composable
fun GroupHeader(title: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f)) // Semi-transparent background
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}


@Composable
fun ExpenseCard(expense: ExpenseEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = expense.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "₹${expense.amount} • ${expense.category}")
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Date: ${expense.date?.toFormattedDate()}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


@PreviewLightDark
@Composable
private fun ExpenseListScreenPreview() {
    ExpenseTrackerTheme {
        ExpenseListScreen(
            expenses = listOf(
                ExpenseEntity(1, "Lunch", 250.0, "Food", "2025-09-17", "Veg thali"),
                ExpenseEntity(2, "Auto", 80.0, "Travel", "2025-09-18", "Office travel"),
            ),
            selectedDate = "2025-10-21",
            totalAmount = 2000.00,
            onDateChange = {},
            displayedItems = emptyList(),
            currentGroupingMode = GroupingMode.BY_DATE,
            {})
    }
}
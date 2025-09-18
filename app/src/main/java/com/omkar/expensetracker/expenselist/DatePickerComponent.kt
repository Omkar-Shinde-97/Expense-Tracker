package com.omkar.expensetracker.expenselist

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.omkar.expensetracker.R
import com.omkar.expensetracker.ui.theme.ExpenseTrackerTheme
import com.omkar.expensetracker.utils.toFormattedDateString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerComponent(
    initialSelectedDateMillis: Long, onDismiss: () -> Unit, onDateChange: (String) -> Unit,
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialSelectedDateMillis
    )
    val confirmEnabled by remember {
        derivedStateOf { datePickerState.selectedDateMillis != null }
    }
    DatePickerDialog(onDismissRequest = {
        onDismiss()
    }, confirmButton = {
        TextButton(
            onClick = {
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
                onDismiss()
            }) {
            Text(stringResource(R.string.cancel))
        }
    }) {
        DatePicker(state = datePickerState)
    }
}

@PreviewLightDark
@Composable
private fun DatePickerComponentPreview() {
    ExpenseTrackerTheme {
        DatePickerComponent(initialSelectedDateMillis = 0L, onDismiss = {}, onDateChange = {})
    }
}
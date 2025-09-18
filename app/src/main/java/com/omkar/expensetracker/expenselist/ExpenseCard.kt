package com.omkar.expensetracker.expenselist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.omkar.expensetracker.database.entity.ExpenseEntity
import com.omkar.expensetracker.ui.theme.ExpenseTrackerTheme
import com.omkar.expensetracker.utils.toFormattedDate

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
            if (!expense.notes.isNullOrBlank()) {
                Text(
                    text = "Note: ${expense.notes}", style = MaterialTheme.typography.bodySmall
                )
            }

        }
    }
}

@PreviewLightDark
@Composable
private fun ExpenseCardPreview() {
    ExpenseTrackerTheme {
        ExpenseCard(
            expense = ExpenseEntity(
                id = 1,
                title = "Lunch",
                amount = 250.0,
                category = "Food",
                date = "2025-09-17",
                notes = "Veg thali"
            )
        )
    }
}
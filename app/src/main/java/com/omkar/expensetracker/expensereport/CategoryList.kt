package com.omkar.expensetracker.expensereport

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.omkar.expensetracker.ui.theme.ExpenseTrackerTheme

@Composable
fun CategoryList(categories: List<CategoryTotal>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        categories.forEach { cat ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(cat.category, style = MaterialTheme.typography.bodyLarge)
                    Text("₹${cat.total}", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun CategoryListPreview() {
    ExpenseTrackerTheme {
        CategoryList(
            categories = listOf(
                CategoryTotal("Category 1", 50.0),
                CategoryTotal("Category 2", 100.0),
                CategoryTotal("Category 3", 150.0),
            )
        )
    }
}
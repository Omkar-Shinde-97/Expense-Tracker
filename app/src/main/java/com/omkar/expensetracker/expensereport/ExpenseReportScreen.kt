package com.omkar.expensetracker.expensereport

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omkar.expensetracker.R
import com.omkar.expensetracker.ui.theme.ExpenseTrackerTheme

data class DailyTotal(val dayOfWeek: String, val total: Double)
data class CategoryTotal(val category: String, val total: Double)

@Composable
fun ExpenseReportScreen(viewModel: ExpenseReportViewModel) {
    val dailyTotals by viewModel.dailyTotals.collectAsStateWithLifecycle()
    val categoryTotals by viewModel.categoryTotals.collectAsStateWithLifecycle()
    ExpenseReportScreenComponent(dailyTotals, categoryTotals)
}

@Composable
fun ExpenseReportScreenComponent(
    dailyTotals: List<DailyTotal>, categoryTotals: List<CategoryTotal>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        item {
            Text(
                stringResource(R.string.daily_totals_title),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            BarChart(dailyTotals.map { it.total }, dailyTotals.map { it.dayOfWeek })
        }

        item {
            Text(
                stringResource(R.string.category_wise_totals_title),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            CategoryList(categoryTotals)
        }
    }
}

@PreviewLightDark
@Composable
private fun ExpenseReportScreenPreview() {
    ExpenseTrackerTheme {
        ExpenseReportScreenComponent(
            dailyTotals = listOf(
                DailyTotal("Mon", 100.0),
                DailyTotal("Tue", 100.0),
                DailyTotal("Wed", 100.0),
                DailyTotal("Thus", 100.0),
                DailyTotal("Fri", 150.0),
                DailyTotal("Sat", 200.0),
                DailyTotal("Sun", 250.0),
            ), categoryTotals = listOf(
                CategoryTotal("Category 1", 50.0),
                CategoryTotal("Category 2", 100.0),
                CategoryTotal("Category 3", 150.0),
            )
        )
    }
}



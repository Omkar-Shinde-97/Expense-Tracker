package com.omkar.expensetracker.expensereport

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
            Text("Daily Totals (Last 7 days)", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            BarChart(dailyTotals.map { it.total }, dailyTotals.map { it.dayOfWeek })
        }

        item {
            Text("Category-wise Totals", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            CategoryList(categoryTotals)
        }
    }
}

@Composable
fun BarChart(values: List<Double>, labels: List<String>) {
    val maxVal = (values.maxOrNull()?.coerceAtLeast(1.0) ?: 1.0)
    val barColor = MaterialTheme.colorScheme.primary
    val chartAreaHeight = 150.dp
    val labelHeight = 50.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(chartAreaHeight + labelHeight),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        values.forEachIndexed { index, value ->
            val barHeightRatio = (value / maxVal).toFloat().coerceIn(0f, 1f)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .height(chartAreaHeight)
                        .graphicsLayer { alpha = 0.99f },
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(fraction = barHeightRatio)
                    ) {
                        if (barHeightRatio > 0f) {
                            drawRect(
                                color = barColor,
                                topLeft = Offset(0f, 0f),
                                size = size
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = labels[index],
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

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
fun ExpenseReportScreenPreview() {
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



package com.omkar.expensetracker.expensereport

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.omkar.expensetracker.ui.theme.ExpenseTrackerTheme

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
                        .graphicsLayer { alpha = 0.99f }, contentAlignment = Alignment.BottomCenter
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(fraction = barHeightRatio)
                    ) {
                        if (barHeightRatio > 0f) {
                            drawRect(
                                color = barColor, topLeft = Offset(0f, 0f), size = size
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = labels[index], style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun BarChartPreview()
{
    ExpenseTrackerTheme {
        BarChart(
            values = listOf(100.0, 200.0, 150.0),
            labels = listOf("Mon", "Tue", "Wed")
        )
    }
}
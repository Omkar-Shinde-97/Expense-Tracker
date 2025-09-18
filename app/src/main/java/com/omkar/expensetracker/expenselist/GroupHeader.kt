package com.omkar.expensetracker.expenselist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.omkar.expensetracker.ui.theme.ExpenseTrackerTheme

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

@PreviewLightDark
@Composable
private fun GroupHeaderPreview() {
    ExpenseTrackerTheme {
        GroupHeader(title = "Group Title")
    }
}
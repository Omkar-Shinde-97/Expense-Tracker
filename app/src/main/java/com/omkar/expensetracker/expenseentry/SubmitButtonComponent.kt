package com.omkar.expensetracker.expenseentry

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.omkar.expensetracker.R
import com.omkar.expensetracker.ui.theme.ExpenseTrackerTheme

@Composable
fun SubmitButtonComponent(titleId: Int, onClick: () -> Unit) {
    Button(
        onClick = {
            onClick()
        }, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()
    ) {
        Text(stringResource(titleId))
    }
}


@PreviewLightDark
@Composable
private fun SubmitButtonComponentPreview() {
    ExpenseTrackerTheme {
        SubmitButtonComponent(titleId = R.string.submit) { }
    }
}
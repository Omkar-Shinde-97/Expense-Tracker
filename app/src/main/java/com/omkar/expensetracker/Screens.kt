package com.omkar.expensetracker

sealed class Screen(val route: String, val title : String) {
    object Entry : Screen("expense_entry", "Add Expense")
    object List : Screen("expense_list", "Expense List")
    object Report : Screen("expense_report", "Report")
}
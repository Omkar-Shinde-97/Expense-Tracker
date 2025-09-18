package com.omkar.expensetracker.database.entity

import androidx.room.PrimaryKey
import androidx.room.Entity


@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Double,
    val category: String? = null,
    val date: String? = null,
    val notes: String? = null,
    val receipt: String? = null
)

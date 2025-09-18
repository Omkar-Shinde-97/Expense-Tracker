package com.omkar.expensetracker.expenseentry

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omkar.expensetracker.ExpenseRepository
import com.omkar.expensetracker.database.entity.ExpenseEntity
import com.omkar.expensetracker.utils.DateUtils.extractDateOnly
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ExpenseEntryViewmodel @Inject constructor(
    private val repository: ExpenseRepository
) : ViewModel() {

    var totalSpentToday = mutableStateOf<Double?>(null)
        private set

    init {
        getTotalSpentToday()
    }

    fun getTotalSpentToday() = viewModelScope.launch(Dispatchers.IO) {
        repository.getTotalSpentByDate(extractDateOnly()).collect {
            withContext(Dispatchers.Main) {
                totalSpentToday.value = it
            }
        }
    }

    fun addExpense(expense: ExpenseEntity) = viewModelScope.launch(Dispatchers.IO) {
        val expenseToSave = expense.copy(
            date = extractDateOnly()
        )
        repository.insertExpense(expenseToSave)
    }
}
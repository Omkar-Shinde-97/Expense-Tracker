package com.omkar.expensetracker.expensereport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omkar.expensetracker.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ExpenseReportViewModel @Inject constructor(
    private val repository: ExpenseRepository
) : ViewModel() {

    val dailyTotals: StateFlow<List<DailyTotal>> =
        repository.getDailyTotals()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categoryTotals: StateFlow<List<CategoryTotal>> =
        repository.getCategoryTotals()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
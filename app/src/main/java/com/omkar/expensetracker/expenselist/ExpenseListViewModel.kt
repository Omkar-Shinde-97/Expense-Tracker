package com.omkar.expensetracker.expenselist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omkar.expensetracker.ExpenseRepository
import com.omkar.expensetracker.database.entity.ExpenseEntity
import com.omkar.expensetracker.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ExpenseListViewModel @Inject constructor(
   private val repository: ExpenseRepository,
) : ViewModel() {

    private val _selectedDate = MutableStateFlow<String?>(DateUtils.extractDateOnly())
    val selectedDate: StateFlow<String?> = _selectedDate

    val expenses: StateFlow<List<ExpenseEntity>> =
        _selectedDate.flatMapLatest { date -> if (date.isNullOrBlank()) repository.getAllExpenses() else repository.getExpensesByDate(date) }
            .stateIn(viewModelScope, SharingStarted.Companion.Lazily, emptyList())

    val totalSpent: StateFlow<Double> =
        _selectedDate.flatMapLatest { date -> if(date.isNullOrBlank()) repository.getTotalSpent() else  repository.getTotalSpentByDate(date) }
            .map { it ?: 0.0 }.stateIn(viewModelScope, SharingStarted.Companion.Lazily, 0.0)

    fun changeDate(date: String?) {
        _selectedDate.update { date }
    }

    private val _groupingMode = MutableStateFlow(GroupingMode.BY_CATEGORY)
    val groupingMode: StateFlow<GroupingMode> = _groupingMode.asStateFlow()

    val displayItem = combine(expenses, _groupingMode, _selectedDate) { expenses, mode, _ ->
        val items = mutableListOf<ExpenseDisplayItem>()
        when (mode) {
            GroupingMode.BY_CATEGORY -> {
                expenses.sortedByDescending { it.date }.groupBy { it.category ?: "" }.toSortedMap()
                    .forEach { (category, expensesInCategory) ->
                        items.add(ExpenseDisplayItem.Header(category ?: ""))
                        expensesInCategory.forEach { expense ->
                            items.add(ExpenseDisplayItem.ExpenseItem(expense))
                        }
                    }
            }

            GroupingMode.BY_DATE -> {
                expenses.sortedByDescending { it.date }.groupBy {
                    it.date
                }.forEach { (dateString, expensesOnDate) ->
                    items.add(ExpenseDisplayItem.Header(dateString ?: ""))
                    expensesOnDate.forEach { expense ->
                        items.add(ExpenseDisplayItem.ExpenseItem(expense))
                    }
                }
            }
        }
        items
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun toggleGroupingMode() {
        _groupingMode.value = when (_groupingMode.value) {
            GroupingMode.BY_CATEGORY -> GroupingMode.BY_DATE
            GroupingMode.BY_DATE -> GroupingMode.BY_CATEGORY
        }
    }

}
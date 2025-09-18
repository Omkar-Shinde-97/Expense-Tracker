package com.omkar.expensetracker

import android.util.Log
import com.omkar.expensetracker.database.dao.ExpenseDao
import com.omkar.expensetracker.database.entity.ExpenseEntity
import com.omkar.expensetracker.expensereport.CategoryTotal
import com.omkar.expensetracker.expensereport.DailyTotal
import com.omkar.expensetracker.utils.DateUtils.getDayOfWeekAbbreviation
import com.omkar.expensetracker.utils.DateUtils.getPastNDaysDateStrings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ExpenseRepository @Inject constructor(private val expenseDao: ExpenseDao) {

    suspend fun insertExpense(expense: ExpenseEntity) {
        expenseDao.insertExpense(expense)
    }

    fun getAllExpenses(): Flow<List<ExpenseEntity>> = expenseDao.getAllExpenses()


    fun getExpensesByDate(date: String): Flow<List<ExpenseEntity>> {
        Log.d("TAG", "getExpensesByDate111: $date ")
        return expenseDao.getExpensesByDate(date)
    }

    fun getTotalSpentByDate(date: String): Flow<Double?> {
        return expenseDao.getTotalSpentByDate(date)
    }

    fun getTotalSpent(): Flow<Double?> {
        return expenseDao.getTotalSpent()
    }

    fun getDailyTotals(): Flow<List<DailyTotal>> {
        val lastSevenDaysDateStrings = getPastNDaysDateStrings(7)

        val dailyTotalFlows: List<Flow<Pair<String, Double?>>> =
            lastSevenDaysDateStrings.map { dateString ->
                expenseDao.getTotalForDateString(dateString)
                    .combine(flowOf(dateString)) { total, dayDateStr ->
                        Pair(dayDateStr, total)
                    }
            }

        return combine(dailyTotalFlows) { totalsArray: Array<Pair<String, Double?>> ->
            val dailyTotalsList = mutableListOf<DailyTotal>()
            totalsArray.forEach { (dateStr, totalAmount) ->
                dailyTotalsList.add(
                    DailyTotal(
                        dayOfWeek = getDayOfWeekAbbreviation(dateStr),
                        total = totalAmount ?: 0.0
                    )
                )
            }
            Log.d("RepoLog", "getDailyTotals emitting: $dailyTotalsList")
            dailyTotalsList
        }
    }

    fun getCategoryTotals(): Flow<List<CategoryTotal>> {
        return expenseDao.getCategoryTotals().map { list ->
            list.map { CategoryTotal(it.category, it.total) }
        }
    }
}

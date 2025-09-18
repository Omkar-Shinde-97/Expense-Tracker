package com.omkar.expensetracker.database.dao

import androidx.room.*
import androidx.room.Insert
import com.omkar.expensetracker.database.entity.ExpenseEntity
import com.omkar.expensetracker.expensereport.CategoryTotal
import com.omkar.expensetracker.expensereport.DailyTotal
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expenseEntity: ExpenseEntity)

    @Query("SELECT * FROM expenses ORDER BY id DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE date == :date ORDER BY id DESC")
    fun getExpensesByDate(date: String): Flow<List<ExpenseEntity>>

    @Query("SELECT SUM(amount) FROM expenses WHERE date == :date")
    fun getTotalSpentByDate(date: String): Flow<Double?>

    @Query("SELECT SUM(amount) FROM expenses")
    fun getTotalSpent(): Flow<Double?>

    @Query("SELECT strftime('%w', date/1000, 'unixepoch') as dayOfWeek, SUM(amount) as total FROM expenses WHERE date >= :sevenDaysAgo GROUP BY dayOfWeek ORDER BY dayOfWeek")
    fun getDailyTotals(sevenDaysAgo: Long): Flow<List<DailyTotal>>

    // ✅ Category totals
    @Query("SELECT category, SUM(amount) as total FROM expenses GROUP BY category")
    fun getCategoryTotals(): Flow<List<CategoryTotal>>

    @Query("SELECT SUM(amount) FROM expenses WHERE date = :dateString")
    fun getTotalForDateString(dateString: String): Flow<Double?> // Returns Flow<Double?>
}

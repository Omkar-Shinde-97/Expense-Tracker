package com.omkar.expensetracker.di

import android.content.Context
import androidx.room.Room
import com.omkar.expensetracker.ExpenseRepository
import com.omkar.expensetracker.MainApplication
import com.omkar.expensetracker.database.ExpenseDatabase
import com.omkar.expensetracker.database.dao.ExpenseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext app: Context): MainApplication {
        return app as MainApplication
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ExpenseDatabase {
        return Room.databaseBuilder(
            context, ExpenseDatabase::class.java, "expense_database"
        ).build()
    }

    @Provides
    fun provideExpenseDao(db: ExpenseDatabase): ExpenseDao = db.expenseDao()

    @Provides
    fun provideExpenseRepository(dao: ExpenseDao): ExpenseRepository {
        return ExpenseRepository(dao)
    }


}

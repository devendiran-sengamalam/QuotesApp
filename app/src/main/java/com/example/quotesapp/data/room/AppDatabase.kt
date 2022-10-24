package com.example.quotesapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.quotesapp.data.model.Quotes
import com.example.quotesapp.data.room.daos.QuotesDao

@Database(entities = [Quotes.QuotesBO::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun quotesDao(): QuotesDao?

}
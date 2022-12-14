package com.example.quotesapp.data.room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase


abstract class DatabaseClient : RoomDatabase() {


    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "MyQuotes")
                .addCallback(
                    object : RoomDatabase.Callback() {
                    }
                )
                .build()

        }


    }

}
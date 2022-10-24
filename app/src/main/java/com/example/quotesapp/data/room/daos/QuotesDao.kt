package com.example.quotesapp.data.room.daos

import androidx.room.*
import com.example.quotesapp.data.model.Quotes

@Dao
interface QuotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuotes(quote: Quotes.QuotesBO)



    @Query("SELECT * FROM quotes_table")
    fun getAllQuotesLiveData(): List<Quotes.QuotesBO>

    @Query("SELECT * FROM quotes_table WHERE _id = :quoteId")
    fun getQuoteByIdLiveData(quoteId: String): Quotes.QuotesBO

}
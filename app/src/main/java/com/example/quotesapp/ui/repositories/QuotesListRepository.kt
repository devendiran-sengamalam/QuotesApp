package com.example.quotesapp.ui.repositories

import android.app.Application
import com.example.quotesapp.data.api.ApiInterface
import com.example.quotesapp.data.api.RetrofitInstance
import com.example.quotesapp.data.api.SafeApiRequest
import com.example.quotesapp.data.model.Quotes
import com.example.quotesapp.data.room.DatabaseClient
import com.example.quotesapp.data.room.daos.QuotesDao

class QuotesListRepository(application: Application ) : SafeApiRequest()
{
//    private val api: ApiInterface
    private val api = RetrofitInstance.getInstance().create(ApiInterface::class.java)
    private val movieDAO: QuotesDao? = DatabaseClient.getInstance(application).quotesDao()
    suspend fun getNewsRemote( ): Quotes
    {
        return apiRequest{ api.getNewsList( ) }
    }
    suspend fun getNews(): List<Quotes.QuotesBO>?
    {
        return movieDAO?.getAllQuotesLiveData()
    }



    suspend fun insertQuote(quote: Quotes.QuotesBO) {
        movieDAO?.insertQuotes(quote)
    }


    suspend fun getQuoteByIdLiveData(quoteId: String): Quotes.QuotesBO? {
        return movieDAO?.getQuoteByIdLiveData(quoteId)
    }

    // Singleton Pattern for Repository.
    companion object {
        /**
         *  This is where the EmployeeRepository all callers will receive. Set it to null at first
         *  and make it private so it can't be directly accessed.
         */
        private var INSTANCE: QuotesListRepository? = null

        /**
         * This method checks whether or not INSTANCE is null. If it's not null, it returns the
         * Singleton INSTANCE. If it is null, it creates a new Object, sets INSTANCE equal to that,
         * and returns INSTANCE. From here on out, this method will now return the same INSTANCE,
         * every time.
         */
        fun getInstance(application: Application): QuotesListRepository = INSTANCE ?: kotlin.run {
            INSTANCE = QuotesListRepository(application = application)
            INSTANCE!!
        }
    }
}
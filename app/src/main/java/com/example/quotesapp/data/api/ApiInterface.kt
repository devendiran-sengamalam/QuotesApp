package com.example.quotesapp.data.api

import com.example.quotesapp.data.model.Quotes
import retrofit2.Response
import retrofit2.http.GET


interface ApiInterface
{


    /*@GET("/sources")
    suspend fun getNewsList( @Query(value = "apiKey") apiKey: String): Response<News>*/
    @GET("/quotes")
    suspend fun getNewsList():Response<Quotes>


}
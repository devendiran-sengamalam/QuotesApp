package com.example.quotesapp.data.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitInstance {
    private var INSTANCE: Retrofit? = null
    private var okkHttpclient :OkHttpClient ?=null
    operator fun invoke(networkConnectionInterceptor: NetworkConnectionInterceptor): ApiInterface
    {
         okkHttpclient = OkHttpClient.Builder()
            .addInterceptor(networkConnectionInterceptor)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()


        return Retrofit.Builder()
            .client(okkHttpclient)
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }

    fun getInstance(): Retrofit = INSTANCE ?: kotlin.run {

        val client: OkHttpClient =OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
        val gson = GsonBuilder()
            .setLenient()
            .create()

        Retrofit.Builder()
            .client(client)
            .baseUrl("https://quotable.io/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    }
}
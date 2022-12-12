package com.example.chrisuascat

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


abstract class RetrofitClient {

    companion object {
        @get:Synchronized
        var instance: Retrofit? = null

        fun getRetrofit() : Retrofit {
            val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client : OkHttpClient = OkHttpClient.Builder().apply {
                addInterceptor(interceptor)
            }.build()

            if(instance == null){
                instance = Retrofit.Builder().baseUrl("https://dummyjson.com/")
                    .addConverterFactory(GsonConverterFactory.create()).client(client)
                    .build()
            }

            return instance!!
        }
    }
}
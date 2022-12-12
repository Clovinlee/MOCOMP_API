package com.example.chrisuascat

import retrofit2.Call
import retrofit2.http.GET

interface CategoryAPI {
    @GET("products/categories")
    fun getAllCategories(
    ): Call<List<String>>
}
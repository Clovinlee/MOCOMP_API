package com.example.chrisuascat

import retrofit2.Call
import retrofit2.http.*

interface ProductsAPI {
    @GET("products")
    fun getAllProducts(
    ): Call<ProductResults>

    @GET("products/search")
    fun searchProducts(
        @Query("q") q : String,
    ): Call<ProductResults>

    @GET("products/category/{category}")
    fun getProductCategories(
        @Path("category") category : String?,
        ): Call<ProductResults>

    @GET("products/{id}")
    fun getAProduct(
        @Path("id") id : String?,
    ): Call<Product>
}
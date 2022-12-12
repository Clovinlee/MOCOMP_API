package com.example.db


import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("weather?q=Surabaya&APPID=b2b0a66e2cf87f0ca54a6931932b5b47")
    fun getWeatherPlaceholder():Call<WeatherResponse>
}
package com.example.db

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    companion object{
        private const val TAG="MainActivity"
    }
    lateinit var tvInfo:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvInfo=findViewById(R.id.textView)
        val client=ApiConfiguration.getApiService().getWeatherPlaceholder()
        client.enqueue(object :Callback<WeatherResponse>{
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful){
                    val responseBody=response.body()
                    if (responseBody != null) {
                        tvInfo.text=responseBody.weather!!.first()!!.description
                    }
                }else{

                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.e(TAG,"onFailureL ${t.message}")
            }

        })
    }
}
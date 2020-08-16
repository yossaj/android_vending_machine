package com.example.vendingmachine.data.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory





object RetrofitBuilder{

    fun buildServiceFor(): ApiService {

        val retrofit = Retrofit.Builder()

        return retrofit
            .baseUrl("https://defualturl/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(ApiService::class.java)
    }

}
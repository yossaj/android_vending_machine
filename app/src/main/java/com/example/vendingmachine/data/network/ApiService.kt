package com.example.vendingmachine.data.network

import com.example.vendingmachine.data.models.*
import com.google.gson.JsonObject
import retrofit2.http.GET

interface ApiService{

    @GET("/v1/images/search?size=full")
    suspend fun getCatPic() : List<CatPic>

    @GET("random")
    suspend fun getDogPic() : DogPic

    @GET("quotes/")
    suspend fun getSwansonWisdom(): List<String>

    @GET("advice")
    suspend fun getAdvice() : Slip

    @GET(" ")
    suspend fun getBull() : Bull



}
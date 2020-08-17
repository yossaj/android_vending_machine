package com.example.vendingmachine.data.network

import com.example.vendingmachine.data.models.*
import com.google.gson.JsonObject
import retrofit2.http.GET

interface ApiService{

    @GET("https://api.thecatapi.com/v1/images/search?size=full")
    suspend fun getCatPic() : List<CatPic>

    @GET("https://dog.ceo/api/breeds/image/random")
    suspend fun getDogPic() : DogPic

    @GET("https://ron-swanson-quotes.herokuapp.com/v2/quotes/")
    suspend fun getSwansonWisdom(): List<String>

    @GET("https://api.adviceslip.com/advice")
    suspend fun getAdvice() : Slip

    @GET("https://corporatebs-generator.sameerkumar.website/")
    suspend fun getBull() : Bull

}
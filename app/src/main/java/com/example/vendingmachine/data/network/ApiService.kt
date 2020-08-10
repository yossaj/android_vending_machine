package com.example.vendingmachine.data.network

import com.example.vendingmachine.data.models.CatPic
import retrofit2.http.GET

interface ApiService{

    @GET("/v1/images/search?size=full")
    suspend fun getCatPic() : List<CatPic>


}
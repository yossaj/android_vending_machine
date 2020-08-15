package com.example.vendingmachine.data.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory





object RetrofitBuilder{

//    fun vend(key: String?) : String{
//        val jsonResult = selectAPI(key)
//        if(jsonResult == CONNECTION_ERROR) return CONNECTION_ERROR
//        when(key){
//            "Mustache" -> {
//                val jsonMustache = JSONArray(jsonResult)
//                return jsonMustache[0].toString()
//            }
//            "Bull" -> {
//                val jsonBull = JSONObject(jsonResult)
//                return jsonBull.getString("phrase")
//            }
//            "Cat" ->{
//                val jsonCatArray = JSONArray(jsonResult)
//                val jsonCat = JSONObject(jsonCatArray[0].toString())
//                return jsonCat.getString("url")
//            }
//            "Dog" ->{
//                val jsonDog = JSONObject(jsonResult)
//                return jsonDog.getString("message")
//            }
//            "Advice" ->{
//                val jsonAdviceObj = JSONObject(jsonResult)
//                val jsonAdvice = JSONObject(jsonAdviceObj.getString("slip"))
//                return jsonAdvice.getString("advice")
//            }
//            else -> return jsonResult
//        }
//    }
//

//    }






    fun buildServiceFor(url : String): ApiService {
        val client = OkHttpClient.Builder().build()

        val retrofit = Retrofit.Builder()

        return retrofit
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(client)
            .build().create(ApiService::class.java)
    }

}
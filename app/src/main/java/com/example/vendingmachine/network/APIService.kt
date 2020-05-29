package com.example.vendingmachine.network

import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


private const val CATS = "https://api.thecatapi.com/v1/images/search?size=full"
    private const val DOGS = "https://dog.ceo/api/breeds/image/random"
    private const val RON_SWANSON = "https://ron-swanson-quotes.herokuapp.com/v2/quotes"
    private const val ADVICE = "https://api.adviceslip.com/advice"
    private const val BULL = "https://corporatebs-generator.sameerkumar.website/"


object APIService{

    fun vend(key: String?) : String{
        val jsonResult = selectAPI(key)
        when(key){
            "Mustache" -> {
                val jsonMustache = JSONArray(jsonResult)
                return jsonMustache[0].toString()
            }
            "Bull" -> {
                val jsonBull = JSONObject(jsonResult)
                return jsonBull.getString("phrase")

            }
            else -> return jsonResult
        }
    }

    fun selectAPI(key : String?) : String{
        when(key){
            "Cat" -> return makeRequest(CATS)
            "Dogs" -> return makeRequest(DOGS)
            "Mustache" -> return makeRequest(RON_SWANSON)
            "Advice" -> return makeRequest(ADVICE)
            "Bull" -> return makeRequest(BULL)
            else -> return "Request Failed: $key is out of stock"
        }
    }


    fun makeRequest(selelctedURL : String): String{

        @Throws(IOException::class)
        fun getResponseFromHttpUrl(): String {
            val url = URL(selelctedURL)
            val urlConnection =
                url.openConnection() as HttpURLConnection
            return try {
                val `in` = urlConnection.inputStream
                val scanner = Scanner(`in`)
                scanner.useDelimiter("\\A")
                val hasInput = scanner.hasNext()
                if (hasInput) {
                    scanner.next()
                } else {
                    return IOException("Unable to reach website").toString()
                }
            } finally {
                urlConnection.disconnect()
            }
        }
        return getResponseFromHttpUrl()
    }


}
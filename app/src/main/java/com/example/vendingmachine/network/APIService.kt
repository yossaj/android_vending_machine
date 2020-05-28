package com.example.vendingmachine.network

import android.content.Context
import com.android.volley.toolbox.Volley
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

    fun vend(): String{

        @Throws(IOException::class)
        fun getResponseFromHttpUrl(): String {
            val url = URL(ADVICE)
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
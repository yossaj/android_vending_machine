package com.example.vendingmachine.utils

object UrlParser{

    private const val CATS = "https://api.thecatapi.com/"
    private const val DOGS = "https://dog.ceo/api/breeds/image/random/"
    private const val RON_SWANSON = "https://ron-swanson-quotes.herokuapp.com/v2/quotes/"
    private const val ADVICE = "https://api.adviceslip.com/advice/"
    private const val BULL = "https://corporatebs-generator.sameerkumar.website/"
    private const val CONNECTION_ERROR = "Connection Error: Unable to reach the website"

        fun selectAPI(key : String?) : String {
            when (key) {
                "Cat" -> return CATS
                "Dogs" -> return DOGS
                "Mustache" -> return RON_SWANSON
                "Advice" -> return ADVICE
                "Bull" -> return BULL
                "Dog" -> return DOGS
                else -> return DOGS
            }
        }
}
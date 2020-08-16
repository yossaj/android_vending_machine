package com.example.vendingmachine.utils

object UrlParser{

    private const val CATS = ""
    private const val DOGS = ""
    private const val RON_SWANSON = ""
    private const val ADVICE = ""
    private const val BULL = ""

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
package com.venderino.vendingmachine.data.models.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CatPic(

    @SerializedName("url")
    @Expose
    val url : String
)
package com.example.vendingmachine.data.models

import androidx.room.Entity

@Entity(tableName = "user")
data class User(
    val username : String
)
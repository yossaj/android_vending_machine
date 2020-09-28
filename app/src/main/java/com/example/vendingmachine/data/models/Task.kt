package com.example.vendingmachine.data.models

data class Task constructor(
    var title: String = "",
    var description: String = "",
    val period : Int = 0,
    val colour : Int = 0,
    var completed: Boolean = false,
    var updatedAt: Long = System.currentTimeMillis(),
    var id: String = "-1"
)
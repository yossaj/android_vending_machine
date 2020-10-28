package com.venderino.vendingmachine.data.models

import java.util.*

data class Habit constructor(
    var title: String = "",
    var max: Int = 1,
    var period : Int = 1,
    var count: Int = 0,
    var updatedAt: Long = System.currentTimeMillis(),
    var createdAt :Long = System.currentTimeMillis(),
    var id: String = UUID.randomUUID().toString()
)
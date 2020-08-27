package com.example.vendingmachine.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "habits")
data class Habit @JvmOverloads constructor(
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "max") var max: Int = 1,
    @ColumnInfo(name = "frequency") var frequency : Int = 1,
    @ColumnInfo(name = "count") var count: Int = 0,
    @ColumnInfo(name = "updateAt") var updatedAt: Long = System.currentTimeMillis(),
    @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString()
)
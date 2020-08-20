package com.example.vendingmachine.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "tasks")
data class Task @JvmOverloads constructor(
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "period") val period : Int = 0,
    @ColumnInfo(name ="colour") val colour : Int = 0,
    @ColumnInfo(name = "complete") var isCompleted: Boolean = false,
    @ColumnInfo(name = "update") var updatedOn: Long = System.currentTimeMillis(),
    @PrimaryKey @ColumnInfo(name = "entryid") var id: String = UUID.randomUUID().toString()
)
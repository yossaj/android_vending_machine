package com.example.vendingmachine.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "tasks")
data class Task @JvmOverloads constructor(
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "content") var content: String = "",
    @ColumnInfo(name = "habit") var habit: Boolean = false,
    @ColumnInfo(name = "habitcount") var habitCount: Int = 0,
    @ColumnInfo(name = "complete") var isCompleted: Boolean = false,
    @ColumnInfo(name = "update") var updatedTime: Long = System.currentTimeMillis(),
    @PrimaryKey @ColumnInfo(name = "entryid") var id: String = UUID.randomUUID().toString()
)
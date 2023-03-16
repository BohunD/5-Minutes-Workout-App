package com.example.a7minutesworkout

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity (tableName = "history_table")
data class HistoryEntity (
    @PrimaryKey (autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo (name = "date")
    var date: String = ""
)
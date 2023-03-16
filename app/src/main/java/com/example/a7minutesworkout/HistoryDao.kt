package com.example.a7minutesworkout

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Insert
    suspend fun insert(historyEntity: HistoryEntity)

    @Delete
    suspend fun delete(historyEntity: HistoryEntity)

    @Query("SELECT * FROM `history_table`")
    fun fetchAllRecords(): Flow<List<HistoryEntity>>

    @Query("SELECT * FROM `history_table` WHERE id = :id")
    fun fetchRecordById(id: Int): Flow<HistoryEntity>

}
package com.example.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.db.entities.AirportSearchHistoryEntity

@Dao
interface AirportSearchHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToHistory(airport: AirportSearchHistoryEntity)

    @Query("select * from airport_search_history where username = :username order by search_time desc limit 2")
    suspend fun getHistory(username: String): List<AirportSearchHistoryEntity>
}
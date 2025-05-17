package com.example.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.db.entities.AirlineEntity

@Dao
interface AirlineDao {

    @Insert
    suspend fun addAirline(airline: AirlineEntity)

    @Query("select * from airlines where iata_code = :iata")
    suspend fun getAirlineByIata(iata: String): AirlineEntity?
}
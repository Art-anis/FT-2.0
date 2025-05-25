package com.example.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.db.entities.AirlineEntity

//dao авиалиний
@Dao
interface AirlineDao {

    //добавление авиалинии
    @Insert
    suspend fun addAirline(airline: AirlineEntity)

    //получение авиалинии по iata
    @Query("select * from airlines where iata_code = :iata")
    suspend fun getAirlineByIata(iata: String): AirlineEntity?
}
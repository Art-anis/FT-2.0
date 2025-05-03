package com.example.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.db.entities.AirportEntity

//dao аэропортов
@Dao
interface AirportDao {

    //добавление аэропорта в БД
    @Insert
    suspend fun addAirport(airport: AirportEntity)

    //поиск по названию или по коду iata
    @Query("select * from airports where airport_name like :query or iata_code like :query order by airport_name")
    suspend fun searchAirports(query: String): List<AirportEntity>

    //получение истории поиска аэропортов
    @Query("select * from airports where last_search != 0 order by last_search desc limit 3")
    suspend fun getHistory(): List<AirportEntity>

    //выбор аэропорта в поиске (отмечается обновлением даты)
    @Query("update airports set last_search = :date where iata_code = :iata")
    suspend fun selectAirport(iata: String, date: Long)
}
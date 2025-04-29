package com.example.db.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.db.entities.AirportEntity

//dao аэропортов
@Dao
interface AirportDao {

    //добавление аэропорта в БД
    @Insert
    fun addAirport(airport: AirportEntity)
}
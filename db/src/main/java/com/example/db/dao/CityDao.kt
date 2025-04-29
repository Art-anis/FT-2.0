package com.example.db.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.db.entities.CityEntity

//dao городов
@Dao
interface CityDao {

    //добавление города
    @Insert
    fun addCity(city: CityEntity)
}
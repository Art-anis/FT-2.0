package com.example.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.db.entities.CityEntity

//dao городов
@Dao
interface CityDao {

    //добавление города
    @Insert
    suspend fun addCity(city: CityEntity)

    //поиск города по коду iata
    @Query("select * from cities where iata_code = :iata")
    suspend fun getCityByIata(iata: String): CityEntity?
}
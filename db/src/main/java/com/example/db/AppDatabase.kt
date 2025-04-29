package com.example.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.db.dao.AirportDao
import com.example.db.dao.CityDao
import com.example.db.entities.AirportEntity
import com.example.db.entities.CityEntity

@Database(entities = [AirportEntity::class, CityEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    //задаем dao в виде абстрактных функций, Room сам сгенерирует реализацию
    abstract fun airportDao(): AirportDao

    abstract fun cityDao(): CityDao
}
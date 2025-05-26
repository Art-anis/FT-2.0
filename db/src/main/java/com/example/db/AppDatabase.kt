package com.example.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.db.dao.AirlineDao
import com.example.db.dao.AirportDao
import com.example.db.dao.CityDao
import com.example.db.dao.TrackedFlightDao
import com.example.db.dao.UserDao
import com.example.db.entities.AirlineEntity
import com.example.db.entities.AirportEntity
import com.example.db.entities.CityEntity
import com.example.db.entities.TrackedFlightEntity
import com.example.db.entities.UserEntity

//класс БД
@Database(entities = [AirportEntity::class, CityEntity::class, TrackedFlightEntity::class, AirlineEntity::class, UserEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    //задаем dao в виде абстрактных функций, Room сам сгенерирует реализацию
    abstract fun airportDao(): AirportDao

    abstract fun cityDao(): CityDao

    abstract fun trackedFlightsDao(): TrackedFlightDao

    abstract fun airlineDao(): AirlineDao

    abstract fun userDao(): UserDao
}
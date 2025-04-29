package com.example.db.di

import android.content.Context
import androidx.room.Room
import com.example.db.AppDatabase
import com.example.db.dao.AirportDao
import com.example.db.dao.CityDao
import org.koin.dsl.module

//создание инстанса БД
fun provideDb(context: Context): AppDatabase {
    return Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "flight_tracker_db"
    ).build()
}

//создание dao
fun provideAirportDao(db: AppDatabase): AirportDao {
    return db.airportDao()
}

fun provideCityDao(db: AppDatabase): CityDao {
    return db.cityDao()
}

val dbModule = module {
    //БД
    single { provideDb(context = get()) }

    //dao
    single { provideAirportDao(db = get()) }
    single { provideCityDao(db = get()) }
}
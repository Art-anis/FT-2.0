package com.example.db.di

import android.content.Context
import androidx.room.Room
import com.example.db.AppDatabase
import com.example.db.dao.AirlineDao
import com.example.db.dao.AirportDao
import com.example.db.dao.CityDao
import com.example.db.dao.TrackedFlightDao
import com.example.db.dao.UserDao
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

fun provideTrackedFlightDao(db: AppDatabase): TrackedFlightDao {
    return db.trackedFlightsDao()
}

fun provideAirlineDao(db: AppDatabase): AirlineDao {
    return db.airlineDao()
}

fun provideUserDao(db: AppDatabase): UserDao {
    return db.userDao()
}

val dbModule = module {
    //БД
    single { provideDb(context = get()) }

    //dao
    single { provideAirportDao(db = get()) }
    single { provideCityDao(db = get()) }
    single { provideTrackedFlightDao(db = get()) }
    single { provideAirlineDao(db = get()) }
    single { provideUserDao(db = get()) }
}
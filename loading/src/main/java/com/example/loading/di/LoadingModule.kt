package com.example.loading.di

import com.example.loading.LoadingRepository
import org.koin.dsl.module

//модуль загрузки данных
val loadingModule = module {
    //репозиторий
    single { LoadingRepository(airportsApi = get(), airportsDao = get(), citiesDao = get()) }
}
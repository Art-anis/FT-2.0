package com.example.users.di

import com.example.users.UserRepository
import org.koin.dsl.module

//модуль для пользователей
val userModule = module {
    //репозиторий
    single { UserRepository(dao = get()) }
}
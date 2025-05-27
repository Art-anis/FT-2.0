package com.example.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

//таблица пользователей
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "username") val userName: String, //имя пользователя
    @ColumnInfo(name = "email") val email: String, //почта
    @ColumnInfo(name = "password") val password: String //пароль
)
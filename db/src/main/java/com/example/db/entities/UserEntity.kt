package com.example.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

//таблица пользователей
@Entity(tableName = "users", indices = [Index(value = ["username"], unique = true)])
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0, //id
    @ColumnInfo(name = "username") val userName: String, //имя пользователя
    @ColumnInfo(name = "email") val email: String, //почта
    @ColumnInfo(name = "password") val password: String //пароль
)
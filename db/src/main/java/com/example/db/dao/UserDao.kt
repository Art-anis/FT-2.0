package com.example.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.db.entities.UserEntity

//dao для пользователей
@Dao
interface UserDao {

    //добавление пользователя
    @Insert
    suspend fun addUser(user: UserEntity)

    //получение пользователя
    @Query("select * from users where username = :username")
    suspend fun getUser(username: String): UserEntity?
}
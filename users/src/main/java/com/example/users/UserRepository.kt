package com.example.users

import com.example.db.dao.UserDao
import com.example.db.entities.UserEntity
import com.toxicbakery.bcrypt.Bcrypt

//репозиторий для пользователей
class UserRepository(
    private val dao: UserDao //dao
) {

    //регистрация
    suspend fun signUp(username: String, email: String, password: String): Boolean {
        //хэшируем пароль
        val hashedPassword = String(Bcrypt.hash(password, 12), Charsets.UTF_8)

        //проверка на существующего пользователя
        val existingUser = dao.getUser(username)

        //если нашли, то возвращаем false
        existingUser?.let {
            return false
        }
        //собираем сущность и добавляем в БД
        val user = UserEntity(
            userName = username,
            email = email,
            password = hashedPassword
        )
        dao.addUser(user)
        return true
    }

    //вход в аккаунт
    suspend fun signIn(username: String, password: String): Boolean {
        //ищем пользователя с этим именем
        val user = dao.getUser(username.trim())
        user?.let {
            //проверяем пароль
            return Bcrypt.verify(password, user.password.toByteArray(Charsets.UTF_8))
        } ?: return false
    }
}
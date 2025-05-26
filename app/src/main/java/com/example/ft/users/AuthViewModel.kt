package com.example.ft.users

import android.preference.PreferenceManager
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ft.App
import com.example.users.UserRepository
import kotlinx.coroutines.async

//viewmodel для авторизации
class AuthViewModel(
    private val repository: UserRepository
): ViewModel() {

    //для фокуса текстовых полей
    val focusRequester = FocusRequester()

    //очистка фокуса
    fun clearFocus(focusManager: FocusManager) {
        focusManager.clearFocus()
    }

    //регистрация
    suspend fun signUp(username: String, email: String, password: String): Boolean {
        //пытаемся зарегистрировать пользователя
        val result = viewModelScope.async {
            repository.signUp(username, email, password)
        }.await()

        //если все хорошо, записываем этого пользователя как активного
        if (result) {
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(App.getInstance().applicationContext)
            sharedPref.edit {
                putString("activeUser", username)
                apply()
            }
        }
        return result
    }

    //вход в аккаунт
    suspend fun signIn(username: String, password: String): Boolean {
        //пытаемся войти
        val result = viewModelScope.async {
            repository.signIn(username, password)
        }.await()

        //если все хорошо, записываем пользователя как активного
        if (result) {
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(App.getInstance().applicationContext)
            sharedPref.edit {
                putString("activeUser", username)
                apply()
            }
        }

        return result
    }
}
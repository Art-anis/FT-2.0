package com.example.ft.users

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.ft.App
import com.example.ft.R

//экран регистрации
@Composable
fun SignUpScreen(
    focusRequester: FocusRequester,
    onClick: (String, String, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 32.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //заголовок
        Text(
            text = stringResource(R.string.sign_up),
            modifier = Modifier.padding(bottom = 64.dp),
            style = MaterialTheme.typography.titleLarge
        )

        //состояния текстовых полей
        var username by rememberSaveable { mutableStateOf("") }
        var email by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }

        //флаг видимости пароля
        var passwordVisible by rememberSaveable { mutableStateOf(false) }

        TextField(
            modifier = Modifier.focusRequester(focusRequester),
            value = username,
            onValueChange = {
                username = it
            },
            label = {
                Text(stringResource(R.string.username))
            },
            placeholder = {
                Text(stringResource(R.string.username_placeholder))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        TextField(
            modifier = Modifier
                .padding(vertical = 32.dp)
                .focusRequester(focusRequester),
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text(stringResource(R.string.email))
            },
            placeholder = {
                Text(stringResource(R.string.email_placeholder))
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            singleLine = true
        )
        TextField(
            modifier = Modifier.focusRequester(focusRequester),
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text(stringResource(R.string.password))
            },
            placeholder = {
                Text(stringResource(R.string.password_hint))
            },
            trailingIcon = {
                //иконка для видимости пароля
                Icon(
                    painter = painterResource(if (passwordVisible) R.drawable.hide_password else R.drawable.show_password),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            passwordVisible = !passwordVisible
                        }
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            visualTransformation = if (!passwordVisible) PasswordVisualTransformation() else VisualTransformation.None
        )

        Spacer(modifier = Modifier.height(64.dp))
        Button(
            onClick = {
                val isDataValid = validateSignUp(
                    username = username,
                    email = email,
                    password = password
                )
                if (isDataValid) {
                    onClick(username, email, password)
                }
            }
        ) {
            Text(stringResource(R.string.sign_up_prompt))
        }
    }
}

fun validateSignUp(username: String, email: String, password: String): Boolean {
    val isUsernameValid = username.isNotEmpty()
    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isPasswordValid = password.length >= 8 && password.contains(Regex("[A-Z]")) && password.contains(Regex("[0-9]"))

    val context = App.getInstance().applicationContext
    if (!isUsernameValid) {
        Toast.makeText(context, context.getString(R.string.empty_username_error), Toast.LENGTH_SHORT).show()
        return false
    }
    if (!isEmailValid) {
        Toast.makeText(context, context.getString(R.string.invalid_email), Toast.LENGTH_SHORT).show()
        return false
    }
    if (!isPasswordValid) {
        if (password.length < 8) {
            Toast.makeText(context, context.getString(R.string.short_password_error), Toast.LENGTH_SHORT).show()
        }
        else if (!password.contains(Regex("[A-Z]"))) {
            Toast.makeText(context, context.getString(R.string.no_capital_letter_error), Toast.LENGTH_SHORT).show()
        }
        else if (!password.contains(Regex("[0-9]"))) {
            Toast.makeText(context, context.getString(R.string.no_digit_error), Toast.LENGTH_SHORT).show()
        }
        return false
    }
    return true
}
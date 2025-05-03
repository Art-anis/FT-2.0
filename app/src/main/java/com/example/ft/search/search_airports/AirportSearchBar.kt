package com.example.ft.search.search_airports

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.ft.R

//поисковая строка для аэропортов
@Composable
fun AirportSearchBar(
    query: TextFieldState, //состояние поисковой строки
    searchAirports: (String) -> Unit, //функция поиска аэропортов
    clearSearch: () -> Unit, //функция очистки запроса
    onNavigateBack: () -> Unit //функция возврвщения на предыдущий экран
) {
    //поисковая строка
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(bottom = 32.dp)
    ) {
        //кнопка "Назад"
        IconButton(onClick = {
            clearSearch()
            onNavigateBack()
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null
            )
        }
        //поисковое поле
        TextField(
            //размеры
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 32.dp),
            //значение
            value = query.text.toString(),
            //в одну строку
            singleLine = true,
            //определяем действие при нажатии Enter
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            //обновление состояния текста
            onValueChange = {
                query.edit { replace(0, length, it) }
                searchAirports(query.text.toString())
            },
            //ведущая иконка
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            //хвостовая иконка для очистки текста
            trailingIcon = {
                IconButton(onClick = {
                    query.clearText()
                    clearSearch()
                }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null
                    )
                }
            },
            //название
            label = {
                Text(stringResource(R.string.search_airport_label))
            },
            //плейсхолдер
            placeholder = {
                Text(stringResource(R.string.search_airport_placeholder))
            }
        )
    }
}
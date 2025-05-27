package com.example.ft.navigation

import android.preference.PreferenceManager
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ft.R

@Composable
fun DrawerHeader() {
    //получаем имя активного пользователя
    val sharedPref = PreferenceManager.getDefaultSharedPreferences(LocalContext.current)
    val username = sharedPref.getString("activeUser", "")
    Column(
        modifier = Modifier.background(color = colorResource(R.color.light_blue))
            .padding(top = 32.dp)
    ) {
        Text(
            text = "Flight Tracker",
            fontSize = 28.sp,
            modifier = Modifier.padding(start = 16.dp)
        )
        //выводим имя текущего пользователя
        if (!username.isNullOrEmpty()) {
            Text(
                text = "Currently logged in as $username",
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 8.dp, top = 16.dp, bottom = 8.dp)
            )
        }
        HorizontalDivider(thickness = 2.dp, color = Color.Black)
    }
}

@Composable
fun DrawerBody(
    selected: String,
    items: List<MenuItem>,
    onItemClick: (MenuItem) -> Unit,
    signOut: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(modifier = Modifier.align(Alignment.TopCenter)) {
            items(items) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onItemClick(item)
                        }
                        .background(if (selected == item.id) Color.LightGray else Color.White)
                        .padding(16.dp)
                ) {
                    Icon(imageVector = item.icon, contentDescription = null)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = item.title,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Surface(
            shadowElevation = 5.dp,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier
                    .border(color = Color.Black, width = 1.dp, shape = RoundedCornerShape(10.dp))
                    .padding(8.dp)
                    .clickable {
                        signOut()
                    }
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = stringResource(R.string.sign_out)
                )
            }
        }
    }
}
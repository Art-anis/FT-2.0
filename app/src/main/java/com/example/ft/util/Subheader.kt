package com.example.ft.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun SubHeader(
    modifier: Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        textAlign = TextAlign.Center,
        text = text,
        fontSize = 20.sp,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
}
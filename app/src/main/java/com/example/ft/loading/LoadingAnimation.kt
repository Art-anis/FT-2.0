package com.example.ft.loading

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

private const val PADDING_MIDDLE_CIRCLE = 0.15f
private const val PADDING_OUTER_CIRCLE = 0.3f
private const val POSITION_START_OFFSET_MIDDLE_CIRCLE = 90f
private const val POSITION_START_OFFSET_OUTER_CIRCLE = 135f

@Composable
fun LoadingAnimation(modifier: Modifier) {
    //состояние бесконечного перехода
    val infiniteTransition = rememberInfiniteTransition()

    //анимация вращения индикатора
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f, //начальное положение
        targetValue = 360f, //конечное положение
        animationSpec = infiniteRepeatable( //настройки анимации
            animation = tween(1000) //продолжительность одного цикла
        ),
        label = ""
    )

    //состояние максимальной ширины индикатора
    var width by remember { mutableIntStateOf(0) }

    //контейнер для индикатора
    Box(
        modifier = modifier
            .onSizeChanged {
                width = it.width
            },
        contentAlignment = Alignment.Center
    ) {
        //внутренний индикатор
        CircularProgressIndicator(
            strokeWidth = 1.dp,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    rotationZ = rotation
                }
        )
        //средний индикатор
        CircularProgressIndicator(
            strokeWidth = 1.dp,
            modifier = Modifier
                .fillMaxSize()
                //увеличение на константу
                .padding(
                    with(LocalDensity.current) {
                        (width * PADDING_MIDDLE_CIRCLE).toDp()
                    }
                )
                //смещение на константу
                .graphicsLayer {
                    rotationZ = rotation + POSITION_START_OFFSET_MIDDLE_CIRCLE
                }
        )
        //внешний индикатор
        CircularProgressIndicator(
            strokeWidth = 1.dp,
            modifier = Modifier
                .fillMaxSize()
                //увеличение на константу
                .padding(
                    with(LocalDensity.current) {
                        (width * PADDING_OUTER_CIRCLE).toDp()
                    }
                )
                //смещение на константу
                .graphicsLayer {
                    rotationZ = rotation + POSITION_START_OFFSET_OUTER_CIRCLE
                }
        )
    }
}
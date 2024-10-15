package com.example.playlists.mainplayer.presentation.components

import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun MusicSlider(
    value: Float,
    onValueChanged: (newValue: Float) -> Unit,
    onValueChangeFinished: () -> Unit,
    songDuration: Long,
) {
    // Ensure songDuration is greater than 0
    val validSongDuration = if (songDuration > 0) songDuration else 1L

    Slider(
        value = value.coerceIn(0f, validSongDuration.toFloat()),
        onValueChange = {
            onValueChanged(it)
        },
        onValueChangeFinished = { onValueChangeFinished() },
        valueRange = 0f..validSongDuration.toFloat(),
        colors = SliderDefaults.colors(
            activeTrackColor = Color.DarkGray,
            inactiveTrackColor = Color.Gray,
            thumbColor = Color.Black
        )
    )
}
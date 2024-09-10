package com.example.playlists.presentation

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MainScreen(innerPadding: PaddingValues) {
    SongList(
        modifier = Modifier.padding(innerPadding),
        PlayListState(),
    )
}
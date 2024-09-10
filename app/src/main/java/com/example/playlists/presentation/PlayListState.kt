package com.example.playlists.presentation

import com.example.playlists.data.Song
import com.example.playlists.data.SongDTO

data class PlayListState(
    val isListLoading: Boolean = false,
    val isSuccess : Song? = null,
    val isError : String = ""
)
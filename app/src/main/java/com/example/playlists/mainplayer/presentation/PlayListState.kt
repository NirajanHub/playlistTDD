package com.example.playlists.mainplayer.presentation

import com.example.playlists.mainplayer.data.Song

data class PlayListState(
    val isListLoading: Boolean = false,
    val isSuccess: Song? = null,
    val isError: String = "",
    val songState: Change = Change.PAUSE,
    val trackPosition : Long = 0,
)


enum class Change {
    NEXT_SONG,
    PREVIOUS_SONG,
    PLAY,
    PAUSE
}
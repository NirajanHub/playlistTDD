package com.example.playlists.presentation

import com.example.playlists.mainplayer.data.Song
import com.example.playlists.mainplayer.presentation.PlayListState
import com.example.playlists.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeViewModel {
    private val _state = MutableStateFlow(PlayListState())
    val state = _state.asStateFlow()

    fun getData(isError: Boolean) {
        _state.update { currentState -> currentState.copy(isListLoading = true) }
        if (isError) {
            _state.update { currentState ->
                currentState.copy(isListLoading = false, isError = "No Internet Connection")
            }
        } else {
            _state.update { currentState ->
                currentState.copy(
                    isListLoading = false,
                    isSuccess = Song(1, icon = "icon", "title", description = "description")
                )
            }
        }
    }
}
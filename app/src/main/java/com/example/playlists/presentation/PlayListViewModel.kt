package com.example.playlists.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlists.data.Song
import com.example.playlists.data.SongDTO
import com.example.playlists.domain.Repository
import com.example.playlists.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlayListViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _state = MutableStateFlow(PlayListState())
    val state = _state.asStateFlow()
    fun fetchSongs() {
        viewModelScope.launch {
            _state.update { currentState -> currentState.copy(isListLoading = true) }
            when (val songs = repository.getSong()) {
                is Result.Error -> _state.update { currentState ->
                    currentState.copy(
                        isListLoading = false,
                        isError = songs.message.toString()
                    )
                }
                is Result.Loading -> _state.update { currentState -> currentState.copy(isListLoading = true) }
                is Result.Success -> _state.update { currentState ->
                    currentState.copy(isListLoading = false, isSuccess = songs.data as Song)
                }
            }
            _state.update { currentState -> currentState.copy(isListLoading = false) }
        }

    }
}


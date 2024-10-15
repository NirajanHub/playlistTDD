package com.example.playlists.mainplayer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlists.mainplayer.data.Song
import com.example.playlists.mainplayer.domain.Repository
import com.example.playlists.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class PlayListViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _state = MutableStateFlow(PlayListState( ))
    val state = _state.asStateFlow()

    init {
        // callRepository()
        fetchFirebaseData()
    }

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
                    currentState.copy(isListLoading = false, isSuccess = songs.data as Song?)
                }
            }
            _state.update { currentState -> currentState.copy(isListLoading = false) }
        }
    }

    fun fetchFirebaseData() {
        viewModelScope.launch {
            repository.getDataFromFirebaseDatabase().collect { result ->
                when (result) {
                    is Result.Success -> _state.update { currentState ->
                        currentState.copy(isSuccess = result.data as Song)
                    }

                    is Result.Error -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isListLoading = false,
                                isError = result.message.toString()
                            )
                        }

                    }

                    is Result.Loading -> _state.update { currentState ->
                        currentState.copy(
                            isListLoading = true
                        )
                    }
                }
            }
            awaitCancellation()
        }
    }

    private fun callRepository() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.writeToFirebaseRealtimeDatabase(
                    Song(
                        id = 1,
                        icon = "icon",
                        title = "name",
                        description = "url"
                    )
                )
            }
        }
    }

    fun setTrackPosition(trackPosition:Long){
        _state.update { currentState ->
            currentState.copy(trackPosition = trackPosition)
        }
    }

    fun playerController(songState: Change) {
        _state.update { currentState ->
            currentState.copy(songState = songState)
        }
    }
}


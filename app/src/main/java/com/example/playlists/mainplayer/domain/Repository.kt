package com.example.playlists.mainplayer.domain

import com.example.playlists.mainplayer.data.Song
import com.example.playlists.mainplayer.data.SongDTO
import com.example.playlists.util.Output
import com.example.playlists.util.Result
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getDataFromServer(): SongDTO?
    suspend fun getDataFromDatabase():Result<Song?>
    suspend fun  getSong(): Result<Any?>
    suspend fun writeToFirebaseRealtimeDatabase(song: Song): Flow<Output>
    fun getDataFromFirebaseDatabase(): Flow<Result<Song>>
}

package com.example.playlists.domain

import com.example.playlists.data.Song
import com.example.playlists.data.SongDTO
import com.example.playlists.util.Result

interface Repository {
    suspend fun getDataFromServer():SongDTO?
    suspend fun getDataFromDatabase():Song?
    suspend fun  getSong(): Result<Any?>
    suspend fun writeToFirebaseRealtimeDatabase(song: Song)
}

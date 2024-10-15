package com.example.playlists.mainplayer.domain

import com.example.playlists.mainplayer.data.Song
import com.example.playlists.util.Output
import com.example.playlists.util.Result
import kotlinx.coroutines.flow.Flow

interface RemoteDatabase {

     fun getDataFromFirebaseRealtime(): Flow<Result<Song>>
     fun writeDataToFirebaseRealtime(song: Song) : Flow<Output>
}
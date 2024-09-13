package com.example.playlists.data.repository

import com.example.playlists.data.PlayListDao
import com.example.playlists.data.Song
import com.example.playlists.data.SongDTO
import com.example.playlists.domain.ApiInterface
import com.example.playlists.domain.RemoteDatabase
import com.example.playlists.domain.Repository
import com.example.playlists.util.Result
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiInterface: ApiInterface,
    private val songDao: PlayListDao,
    private val dataAccess: RemoteDatabase
) : Repository {

    override suspend fun getDataFromServer(): SongDTO? {
        return apiInterface.getAllSong()
    }

    override suspend fun getDataFromDatabase(): Song? {
        return songDao.getAllSongs()
    }

    override suspend fun getSong(): Result<Song?> {
        if (apiInterface.getAllSong()?.id == null) {
            if (songDao.getAllSongs()?.id != null) {
                try {
                    val songs = songDao.getAllSongs()
                    return Result.Success(songs)
                } catch (e: Exception) {
                    return Result.Error(e.message)
                }
            }
        }
        return Result.Error("No Internet Connection")
    }

    override suspend fun writeToFirebaseRealtimeDatabase(song: Song) {
        dataAccess.writeDataToFirebaseRealtime(song)
    }
}
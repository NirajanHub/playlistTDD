package com.example.playlists.data

import com.example.playlists.domain.Repository
import com.example.playlists.util.Result
import kotlinx.coroutines.delay

class FakeRepository : Repository {

    var shouldReturnError = false
    override suspend fun getDataFromServer(): SongDTO{
        return SongDTO(id = 1, icon = "icon", title = "title",description = "description")
    }

    override suspend fun getDataFromDatabase(): Song {
        return Song(id = 1, icon = "icon", title = "title",description = "description")
    }

    override suspend fun getSong(): Result<Any> {
        delay(3000)
        if (shouldReturnError) {
            return Result.Error("No Internet Connection")
        }
        return Result.Success<Song>(Song(id = 1, icon = "icon", title = "title",description = "description"))
    }

    override suspend fun writeToFirebaseRealtimeDatabase(song: Song) {
       //
    }

    fun getSongFailure(): Result<String?> {
        return Result.Error("No Internet Connection")
    }
}
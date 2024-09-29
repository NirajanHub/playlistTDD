package com.example.playlists.data

import com.example.playlists.domain.Repository
import com.example.playlists.util.Output
import com.example.playlists.util.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRepository : Repository {

    var shouldReturnError = false
    override suspend fun getDataFromServer(): SongDTO {
        return SongDTO(id = 1, icon = "icon", title = "title", description = "description")
    }

    override suspend fun getDataFromDatabase(): Result<Song> {
        return Result.Success(
            Song(
                id = 1,
                icon = "icon",
                title = "title",
                description = "description"
            )
        )
    }

    override suspend fun getSong(): Result<Any> {
        delay(3000)
        if (shouldReturnError) {
            return Result.Error("No Internet Connection")
        }
        return Result.Success<Song>(
            Song(
                id = 1,
                icon = "icon",
                title = "title",
                description = "description"
            )
        )
    }

    override suspend fun writeToFirebaseRealtimeDatabase(song: Song): Flow<Output> {
        return flow {
            Output.SUCCESS
        }
    }

    override fun getDataFromFirebaseDatabase(): Flow<Result<Song>> {
        TODO("Not yet implemented")
    }

    fun getSongFailure(): Result<String?> {
        return Result.Error("No Internet Connection")
    }

}
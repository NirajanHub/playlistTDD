package com.example.playlists.data

import com.example.playlists.mainplayer.domain.Repository
import com.example.playlists.mainplayer.data.Song
import com.example.playlists.mainplayer.data.SongDTO
import com.example.playlists.util.Output
import com.example.playlists.util.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRepository : Repository {

    enum class ChooseResult {
        Success,
        Loading,
        Failure
    }

    var shouldReturnError = ChooseResult.Success


    private val song = Song(
        id = 1,
        icon = "icon",
        title = "title",
        description = "description"
    )

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
        if (shouldReturnError == ChooseResult.Failure) {
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
        return flow {
            when (shouldReturnError) {
                ChooseResult.Success -> {
                    emit(Result.Success(song))
                }
                ChooseResult.Failure -> {
                    emit(Result.Error("Error"))
                }
                else -> {
                    emit(Result.Loading)
                }
            }
        }
    }


    fun getSongFailure(): Result<String?> {
        return Result.Error("No Internet Connection")
    }

}
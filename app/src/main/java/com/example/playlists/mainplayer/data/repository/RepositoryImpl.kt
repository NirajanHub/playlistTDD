package com.example.playlists.mainplayer.data.repository

import com.example.playlists.mainplayer.data.PlayListDao
import com.example.playlists.mainplayer.data.Song
import com.example.playlists.mainplayer.data.SongDTO
import com.example.playlists.mainplayer.domain.RemoteDatabase
import com.example.playlists.mainplayer.domain.Repository
import com.example.playlists.util.Output
import com.example.playlists.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    //   private val apiInterface: ApiInterface,
    private val songDao: PlayListDao,
    private val remoteDatabase: RemoteDatabase
) : Repository {


    override suspend fun getDataFromDatabase(): Result<Song> {
          val result =  songDao.getAllSongs()
        return if(result != null){
            Result.Success(result)
        }else{
            Result.Error("Error")
        }
    }

    override suspend fun writeToFirebaseRealtimeDatabase(song: Song) :Flow<Output>{
        return remoteDatabase.writeDataToFirebaseRealtime(song)
    }

    override fun getDataFromFirebaseDatabase(): Flow<Result<Song>> {
        return remoteDatabase.getDataFromFirebaseRealtime()
    }



    //Not Implemented

    override suspend fun getSong(): Result<Song?> {
//        if (apiInterface.getAllSong()?.id == null) {
//            if (songDao.getAllSongs()?.id != null) {
//                try {
//                    val songs = songDao.getAllSongs()
//                    return Result.Success(songs)
//                } catch (e: Exception) {
//                    return Result.Error(e.message)
//                }
//            }
//        }
//        return Result.Error("No Internet Connection")
        return Result.Success(null)
    }

    override suspend fun getDataFromServer(): SongDTO? {
        return null
    }
}
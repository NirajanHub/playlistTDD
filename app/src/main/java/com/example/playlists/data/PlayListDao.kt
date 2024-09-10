package com.example.playlists.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PlayListDao {
    @Query("SELECT * FROM  song")
    fun getAllSongs(): Song?

    @Insert
    fun insertAllSong(vararg songs: Song)

    @Insert
    fun insertSong(song: Song)

    @Delete
    fun deleteSong(song: Song)
}
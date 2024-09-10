package com.example.playlists.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Song::class], version = 1, exportSchema = false)
abstract class PlayListDatabase : RoomDatabase() {
   abstract fun dao(): PlayListDao
}

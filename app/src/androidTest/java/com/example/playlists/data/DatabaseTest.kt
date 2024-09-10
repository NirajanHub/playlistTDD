package com.example.playlists.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DatabaseTest {

    private lateinit var db: PlayListDatabase
    private lateinit var dao: PlayListDao
    private val playList = Song(id = 1,icon = "test", title = "test", description = "test")

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, PlayListDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.dao()
    }

    @After
    fun closeDatabase() {
        if (::db.isInitialized) {
            db.close()
        }
    }

    @Test
    fun test_insertAndRead_returnsSuccess() {
        dao.insertAllSong(playList)

        val allSongs = dao.getAllSongs()

        assertEquals(allSongs?.id,playList.id)
    }

    @Test
    fun test_insertAndRead_returnsFailure() {
        dao.insertAllSong(playList)
        val allSongs = dao.getAllSongs()

        assertEquals(allSongs?.id,playList.id)
    }

    @Test
    fun delete_song_assertsDeleted() {
         dao.deleteSong(playList)

        val allSongs = dao.getAllSongs()
        println(allSongs)
        assertEquals(allSongs?.id,null)
    }
}


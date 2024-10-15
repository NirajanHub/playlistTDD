package com.example.playlists.mainplayer.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Song (
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0,
    @ColumnInfo(name = "icon")
    var icon:String ="",
    @ColumnInfo(name = "title")
    var title:String ="",
    @ColumnInfo(name = "description")
    var description:String="")


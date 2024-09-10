package com.example.playlists.domain

import com.example.playlists.data.Song

interface DataAccess {

     fun getDataFromFirebaseRealtime(callback: (Song?) -> Unit)
     fun writeDataToFirebaseRealtime(song: Song)
}
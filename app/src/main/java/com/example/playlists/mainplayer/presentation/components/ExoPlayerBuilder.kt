package com.example.playlists.mainplayer.presentation.components

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer

class ExoPlayerBuilder {
    fun getExoPlayer(context: Context) : ExoPlayer{
        val player: ExoPlayer = ExoPlayer.Builder(context).build()
        return player
    }
}
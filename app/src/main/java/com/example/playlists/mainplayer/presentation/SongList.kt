package com.example.playlists.mainplayer.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Player.Listener
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.rememberAsyncImagePainter
import com.example.playlists.mainplayer.presentation.components.ControlButtonActions
import com.example.playlists.mainplayer.presentation.components.ControlButtons
import com.example.playlists.mainplayer.presentation.components.ExoPlayerBuilder
import com.example.playlists.mainplayer.presentation.components.MusicSlider
import com.example.playlists.mainplayer.presentation.components.RotatingRecordAnimation
import com.ncodes.playlists.R
import kotlinx.coroutines.delay
import kotlin.math.roundToLong

object Constants {
    val TAG = "SongList Screen"
    val TEST_TAG = "song list"
    val TEST_TAG_ERROR = "ErrorView"
}

@Composable
fun SongList(
    viewModel: PlayListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    var duration by remember {
        mutableLongStateOf(0)
    }

    val context = LocalContext.current
    val exo by remember {
        mutableStateOf(ExoPlayerBuilder().getExoPlayer(context))
    }
    if (state.isError == stringResource(id = R.string.internet_issue)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .testTag(Constants.TEST_TAG_ERROR)
        ) {
            Text(text = stringResource(id = R.string.internet_issue))
        }
    } else {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            RotatingRecordAnimation(painter = painterResource(id = R.drawable.ic_launcher_background))
            LaunchedEffect(key1 = state.isSuccess) {
                val song = state.isSuccess?.description.toString()
                if (song.isNotEmpty()) {
                    val mediaItem = MediaItem.fromUri(state.isSuccess?.description.toString())
                    exo.setMediaItem(mediaItem)
                    exo.setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                            .setUsage(C.USAGE_MEDIA)
                            .build(),
                        true
                    )
                    exo.prepare()
                    exo.addListener(object : Listener {
                        override fun onPlaybackStateChanged(playbackState: Int) {
                            if (playbackState == Player.STATE_READY) {
                                duration = exo.duration
                                if (duration != C.TIME_UNSET) {
                                    Log.d("Exo Player", "Duration Set Correctly")
                                } else {
                                    Log.d("Exo Player", "Duration Not Set Correctly")
                                }
                            }
                        }

                        override fun onPlayerError(error: PlaybackException) {
                            Log.e("Exo Player", "Duration not available")
                        }
                    }

                    )
                    duration = exo.duration
                    Log.d("Duration", " $duration")
                }
            }
            LaunchedEffect(key1 = state.songState) {
                when (state.songState) {
                    Change.NEXT_SONG -> exo.seekToNext()
                    Change.PREVIOUS_SONG -> exo.seekToPrevious()
                    Change.PLAY -> exo.play()
                    Change.PAUSE -> exo.pause()
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                Image(
                    painter =
                    rememberAsyncImagePainter(
                        model = state.isSuccess?.icon,
                    ),
                    contentDescription = stringResource(id = R.string.app_name)
                )

                Text(
                    text = viewModel.state.value.isSuccess?.title
                        ?: stringResource(id = R.string.server_error)
                )
            }
            ControlButtons(songState = state.songState,
                onClick = { actions ->
                    when (actions) {
                        ControlButtonActions.PREVIOUS -> viewModel.playerController(Change.PREVIOUS_SONG)
                        ControlButtonActions.NEXT -> viewModel.playerController(Change.NEXT_SONG)
                        ControlButtonActions.PLAY -> viewModel.playerController(Change.PLAY)
                        ControlButtonActions.PAUSE -> viewModel.playerController(Change.PAUSE)
                    }
                }
            )
            TrackSlider(state = state, viewModel = viewModel, exo = exo, duration = duration)
        }
    }
}

@Composable
fun TrackSlider(
    state: PlayListState,
    viewModel: PlayListViewModel,
    exo: ExoPlayer,
    duration: Long,
) {

    var sliderPosition by remember {
        mutableFloatStateOf(0f)
    }

//    LaunchedEffect(key1 = true) {
//        snapshotFlow {
//            exo.currentPosition
//        }.collect{
//            currentPosition = exo.currentPosition.toFloat()
//        }
//    }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            sliderPosition = exo.currentPosition.toFloat()
        }
    }
    Box(modifier = Modifier.padding(10.dp)) {
        MusicSlider(
            value = sliderPosition.roundToLong().toFloat(),
            onValueChanged = {
                sliderPosition = it
            },
            onValueChangeFinished = {
                viewModel.setTrackPosition(sliderPosition.toLong())
                exo.seekTo(sliderPosition.toLong())
            },
            songDuration = if (duration > 0) duration else 0,
        )
    }
}

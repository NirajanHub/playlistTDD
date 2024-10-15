package com.example.playlists.mainplayer.presentation.components

import SingleControlButton
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import com.example.playlists.mainplayer.presentation.Change
import com.ncodes.playlists.R


@Composable
fun ControlButtons(
    onClick: (value: ControlButtonActions) -> Unit,
    songState: Change
) {
    Row {
        SingleControlButton(
            resource = R.drawable.ic_skip_previous,
            controlButtonActions = ControlButtonActions.PREVIOUS,
            contentDescription = R.string.previous,
            onClick = onClick
        )
        Box {
            if (songState == Change.PAUSE) {
                SingleControlButton(
                    resource = R.drawable.ic_play_circle_outline,
                    controlButtonActions = ControlButtonActions.PLAY,
                    onClick = onClick,
                    contentDescription = R.string.play,
                )
            } else {
                SingleControlButton(
                    resource = R.drawable.ic_pause_circle_outline,
                    controlButtonActions = ControlButtonActions.PAUSE,
                    onClick = onClick,
                    contentDescription = R.string.pause,
                )
            }
        }
        SingleControlButton(
            resource = R.drawable.ic_skip_next,
            controlButtonActions = ControlButtonActions.NEXT,
            onClick = onClick,
            contentDescription = R.string.next,
        )
    }
}

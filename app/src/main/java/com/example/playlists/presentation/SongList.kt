package com.example.playlists.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import coil.compose.rememberAsyncImagePainter


@Composable
fun SongList(
    modifier: Modifier = Modifier,
    state: PlayListState,
) {

    if (state.isError == "No Internet Connection") {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .testTag("ErrorView")
        ) {
            Text(text = "No Internet Connection")
        }
    } else {
        LazyColumn(
            modifier = modifier.testTag("songList")
        ) {
            items(1) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter =
                        rememberAsyncImagePainter(
                            model = state.isSuccess?.icon,
                        ),
                        contentDescription = "icon"
                    )
                    Text(text = "Photo$it")
                }
            }
        }
    }
}


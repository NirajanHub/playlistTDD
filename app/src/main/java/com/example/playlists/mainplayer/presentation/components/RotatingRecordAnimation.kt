package com.example.playlists.mainplayer.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.ncodes.playlists.R

@Composable
fun RotatingRecordAnimation(
    modifier: Modifier = Modifier,
    rotatingDegree: Float = 0f,
    painter: Painter
) {

    val roundShape = object : Shape {
        override fun createOutline(
            size: Size,
            layoutDirection: LayoutDirection,
            density: Density
        ): Outline {
            val p1 = Path().apply {
                addOval(Rect(4f, 3f, size.width - 1, size.height - 1))
            }
            val thickness = size.height / 2.10f
            val p2 = Path().apply {
                addOval(Rect(thickness, thickness, size.width - thickness, size.height - thickness))
            }
            val p3 = Path()
            p3.op(p1, p2, PathOperation.Difference)
            return Outline.Generic(p3)
        }
    }
    Box(
        modifier = modifier
            .aspectRatio(1.0f)
            .clip(roundShape)
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .rotate(rotatingDegree),
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "vinlyn background"
        )
        Image(
            modifier = Modifier
                .fillMaxSize(0.5f)
                .rotate(10f)
                .aspectRatio(1.0f)
                .align(Alignment.Center)
                .clip (roundShape),
            painter = painter,
            contentDescription = "song album cover"
        )
    }
}




import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.playlists.mainplayer.presentation.components.ControlButtonActions


@Composable
fun SingleControlButton(
    resource: Int,
    contentDescription: Int,
    controlButtonActions: ControlButtonActions,
    onClick: (value: ControlButtonActions) -> Unit
) {
    Box(
        modifier = Modifier
            .clickable(onClick = {
                when (controlButtonActions) {
                    ControlButtonActions.NEXT -> onClick(ControlButtonActions.NEXT)
                    ControlButtonActions.PREVIOUS -> onClick(ControlButtonActions.PREVIOUS)
                    ControlButtonActions.PAUSE -> onClick(ControlButtonActions.PAUSE)
                    ControlButtonActions.PLAY -> onClick(ControlButtonActions.PLAY)
                }
            })
            .clip(CircleShape)
            .size(100.dp)
            .padding(10.dp)
    ) {
        Icon(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = resource),
            contentDescription = stringResource(id = contentDescription)
        )
    }
}
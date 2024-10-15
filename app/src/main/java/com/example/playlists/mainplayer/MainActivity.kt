package com.example.playlists.mainplayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.playlists.mainplayer.presentation.MainScreen
import com.example.playlists.mainplayer.presentation.PlayListViewModel
import com.example.playlists.ui.theme.MyApplicationTheme
import com.ncodes.playlists.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: PlayListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    Text(text = stringResource(id = R.string.app_name))
                }) { innerPadding ->
                    MainScreen(innerPadding)
                }
            }
        }
    }
}


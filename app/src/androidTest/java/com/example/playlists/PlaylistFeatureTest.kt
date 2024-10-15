package com.example.playlists

import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.playlists.mainplayer.data.Song
import com.example.playlists.presentation.FakeViewModel
import com.example.playlists.mainplayer.MainActivity
import com.example.playlists.mainplayer.presentation.PlayListState
import com.example.playlists.mainplayer.presentation.SongList
import com.ncodes.playlists.R

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class PlaylistFeatureTest {
    ///Activity Test Rule deprecated.

    ///Replaced by ActivityScenarioRule

    //Compose Rule


    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    private val song = Song(id = 1, icon = "Photo1", title = "Title1", description = "Description1")

    @Test
    fun displayScreenTitle() {
        val expectedText = composeTestRule.activity.getString(R.string.app_name)
        composeTestRule.onNodeWithText(expectedText)
    }

    @Test
    fun show_playlist_when_data_is_loaded() {
        composeTestRule.onNodeWithTag("lazy_playlist")
    }

//    @Test
//    fun showPhoto_onEachItem_when_data_is_loaded() {
//        composeTestRule.activity.setContent {
//            SongList(
//                Modifier, state = PlayListState(isListLoading = true, isSuccess = song)
//            )
//        }
//        composeTestRule.onNodeWithText("Photo1").assertIsDisplayed()
//    }

    @Test
    fun show_error_when_no_data() {
        val viewModel = FakeViewModel()
        viewModel.getData(true)
        composeTestRule.activity.setContent {
            SongList(Modifier, state = PlayListState(isError = "No Internet Connection"))
        }
        composeTestRule.onNodeWithTag("ErrorView").assertIsDisplayed()
    }

//    @Test
//    fun testPlaylistLayout() {
//        composeTestRule.activity.setContent {
//            SongList(state = PlayListState(isListLoading = false, isSuccess = song))
//        }
//        composeTestRule.onNodeWithContentDescription("icon").assertIsDisplayed()
//        composeTestRule.onNodeWithText("Photo1").assertIsDisplayed()
//    }

    @Test
    fun test_not_visible_playlist() {
        composeTestRule.activity.setContent {
            SongList(state = PlayListState(isListLoading = false, isSuccess = song))
        }
        composeTestRule.onNodeWithText("icon2").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Photo2").assertIsNotDisplayed()
    }
}
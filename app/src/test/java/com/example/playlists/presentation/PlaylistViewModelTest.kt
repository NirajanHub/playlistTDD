package com.example.playlists.presentation

import app.cash.turbine.test
import com.example.playlists.MainCoroutineRule
import com.example.playlists.data.FakeRepository
import com.example.playlists.data.Song
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class PlaylistViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: PlayListViewModel
    private lateinit var repository: FakeRepository

    @Before
    fun setUp() {
        repository = FakeRepository()
        viewModel = PlayListViewModel(repository)
    }

    @Test
    fun `test fetchSongs function with success result`() = mainCoroutineRule.testScope.runTest {

        //Given
        val song = Song(id = 1, icon = "icon", title = "title", description = "description")

        //When
        viewModel.fetchSongs()
        //advanceTimeBy(3001)

        advanceUntilIdle()
        //Result
        assertEquals(song, viewModel.state.value.isSuccess)
    }


    @Test
    fun `test the loading functionality of view model`() = mainCoroutineRule.testScope.runTest {

        val song = Song(id = 1, icon = "icon", title = "title", description = "description")
        viewModel.state.test {
            assertFalse(awaitItem().isListLoading)

            viewModel.fetchSongs()
            assertTrue(awaitItem().isListLoading)

            assertFalse(awaitItem().isListLoading)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `test fetchSongs function with error result`() = mainCoroutineRule.testScope.runTest {

        //Given
        val expected = "No Internet Connection"
        repository.shouldReturnError = true
        //When
        viewModel.fetchSongs()
      //  advanceTimeBy(3001)
        advanceUntilIdle()
        repository.shouldReturnError = false
        //Result

        assertEquals(expected,viewModel.state.value.isError)

    }
}
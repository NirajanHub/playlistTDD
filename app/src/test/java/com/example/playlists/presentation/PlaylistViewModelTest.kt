package com.example.playlists.presentation

import app.cash.turbine.test
import com.example.playlists.MainCoroutineRule
import com.example.playlists.data.FakeRepository
import com.example.playlists.mainplayer.data.Song
import com.example.playlists.mainplayer.domain.Repository
import com.example.playlists.mainplayer.presentation.PlayListViewModel
import com.example.playlists.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever


@ExperimentalCoroutinesApi
class PlaylistViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: PlayListViewModel

    private lateinit var fakeRepository: FakeRepository

    @Mock
    private lateinit var mockRepository: Repository

    @Before
    fun setUp() {
        fakeRepository = FakeRepository()
        mockRepository = mock()
        viewModel = PlayListViewModel(fakeRepository)
    }

    private val song = Song(id = 1, icon = "icon", title = "title", description = "description")

    @Test
    fun `test fetchSongs function with success result`() = mainCoroutineRule.testScope.runTest {
        //Given

        //When
        viewModel.fetchSongs()
        //advanceTimeBy(3001)

        advanceUntilIdle()
        //Result
        assertEquals(song, viewModel.state.value.isSuccess)
    }


    @Test
    fun `test the loading functionality of view model`() = mainCoroutineRule.testScope.runTest {

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

        //Arrange
        val expected = "No Internet Connection"
        fakeRepository.shouldReturnError = FakeRepository.ChooseResult.Failure

        //Act
        viewModel.fetchSongs()

        advanceUntilIdle()
        fakeRepository.shouldReturnError = FakeRepository.ChooseResult.Success

        //Assert
        assertEquals(expected, viewModel.state.value.isError)

    }

    @Test
    fun `fetch firebase_data_changes success`() = mainCoroutineRule.testScope.runTest {
        //Arrange
        val expected = song
        whenever(mockRepository.getDataFromFirebaseDatabase()).thenReturn(flow { Result.Success(song) })

        //Act
        viewModel.fetchFirebaseData()

        advanceUntilIdle()

        //Assert
        viewModel.state.test {
            val item = awaitItem()
            assertEquals(song,item.isSuccess)
        }
    }

    @Test
    fun `fetch firebase_data_changes failure`() = mainCoroutineRule.testScope.runTest {
        //Arrange
        val expected = "Error"

        fakeRepository.shouldReturnError = FakeRepository.ChooseResult.Failure
        //Act
        viewModel.fetchFirebaseData()

        advanceUntilIdle()
        fakeRepository.shouldReturnError = FakeRepository.ChooseResult.Success

        //Assert
        viewModel.state.test {
            val item = awaitItem()
            assertEquals(expected,item.isError)
        }
    }

    @Test
    fun `fetch firebase_data_changes loading`() = mainCoroutineRule.testScope.runTest {
        //Arrange
        val expected = true

        fakeRepository.shouldReturnError = FakeRepository.ChooseResult.Loading
        //Act
        viewModel.fetchFirebaseData()

        advanceUntilIdle()
        fakeRepository.shouldReturnError = FakeRepository.ChooseResult.Success
        //Assert
        viewModel.state.test {
            val item = awaitItem()
            assertEquals(expected,item.isListLoading)
        }
    }
}
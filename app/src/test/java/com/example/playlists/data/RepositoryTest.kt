package com.example.playlists.data

import app.cash.turbine.test
import com.example.playlists.data.repository.RepositoryImpl
import com.example.playlists.domain.ApiInterface
import com.example.playlists.domain.RemoteDatabase
import com.example.playlists.util.Result
import com.google.firebase.database.DatabaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.whenever


class RepositoryTest {

    @get:Rule
    val mockito: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var localData: PlayListDao
    private lateinit var repositoryConcrete: RepositoryImpl
    private lateinit var fakeRepository: FakeRepository


    @Mock
    private lateinit var remoteDatabaseMock: RemoteDatabase

    val song = Song(id = 1, icon = "icon", title = "name", description = "url")

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        fakeRepository = FakeRepository()
        // apiInterface = Mockito.mock(ApiInterface::class.java)
        localData = Mockito.mock(PlayListDao::class.java)
        repositoryConcrete = RepositoryImpl(localData, remoteDatabaseMock)
    }

    @Test
    fun `get local database data for Success`() = runTest {
        `when`(localData.getAllSongs()).thenReturn(song)

        val songs = repositoryConcrete.getDataFromDatabase()

        assertEquals(songs, Result.Success(song))
    }

    @Test
    fun `get data from local database failure`() = runTest {
        //Arrange
        whenever(localData.getAllSongs()).thenReturn(null)

        //Act
        val songs = repositoryConcrete.getDataFromDatabase()

        //Assert
        assertEquals(songs, Result.Error<String>("Error"))
    }

    @Test
    fun `test write to firebaseRealtime success`() = runTest {

        //Arrange

        //Act
        repositoryConcrete.writeToFirebaseRealtimeDatabase(song)

        //Assert
        verify(remoteDatabaseMock).writeDataToFirebaseRealtime(song)
    }

    @Test
    fun `test write to firebaseRealtime failure`() = runTest {
        //Arrange
        whenever(remoteDatabaseMock.writeDataToFirebaseRealtime(song)).thenThrow(DatabaseException("Error"))
        // Act and Assert
        val exception = assertThrows(RuntimeException::class.java) {
            runBlocking {
                repositoryConcrete.writeToFirebaseRealtimeDatabase(song)
            }
        }
        //Assert
        assertEquals("Error",exception.message)
    }

    @Test
    fun `getDataFromFirebaseDatabase for success`() = runTest{
        //Arrange
        whenever(remoteDatabaseMock.getDataFromFirebaseRealtime()).thenReturn(flow { emit (Result.Success(song)) })

        //Act
        repositoryConcrete.getDataFromFirebaseDatabase().test {
            val value = awaitItem()
            assertEquals(value,Result.Success(song))
            awaitComplete()
        }
    }

    @Test
    fun `getDataFromFirebaseDatabase for failure`() = runTest{
        //Arrange
        whenever(remoteDatabaseMock.getDataFromFirebaseRealtime()).thenReturn(flow { emit (Result.Error("Error")) })

        //Act
        repositoryConcrete.getDataFromFirebaseDatabase().test {
            val value = awaitItem()
            assertEquals(value,Result.Error<String>("Error"))
            awaitComplete()
        }
    }

    //
//    @Test
//    fun `test get song when rest api fails success Result`() = runTest {
//        val expectedData = Song(id = 1, icon = "icon", title = "name", description = "url")
//        `when`(localData.getAllSongs()).thenReturn(expectedData)
//        `when`(apiInterface.getAllSong()).thenReturn(null)
//        when (val actualData: Result<Song?> = repositoryConcrete.getSong()) {
//            is Result.Success -> {
//                assertEquals(actualData.data?.id, expectedData.id)
//            }
//
//            else -> {
//                Assert.fail()
//            }
//        }
//    }

//    @Test
//    fun `test get song when rest api fails failure Result`() = runTest {
//        val expectedData = "No Internet Connection"
//        when (val actualData: Result<String?> = fakeRepository.getSongFailure()) {
//            is Result.Error -> {
//                assertEquals(actualData.message, expectedData)
//            }
//
//            else -> {
//                Assert.fail()
//            }
//        }
//    }


//    @Test
//    fun `test repository to get data from remote server `() = runTest {
//        val expectedData = SongDTO(id = 1, icon = "icon", title = "name", description = "url")
//        `when`(apiInterface.getAllSong()).thenReturn(expectedData)
//
//        val songs = repositoryConcrete.getDataFromServer()
//
//        assertEquals(songs?.id, expectedData.id)
//    }
}

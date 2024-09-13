package com.example.playlists.data

import com.example.playlists.data.repository.RepositoryImpl
import com.example.playlists.domain.ApiInterface
import com.example.playlists.domain.RemoteDatabase
import com.example.playlists.util.Result
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
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


class RepositoryTest {

    @get:Rule
    val mockito: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var apiInterface: ApiInterface
    private lateinit var localData: PlayListDao
    private lateinit var repositoryConcrete: RepositoryImpl
    private lateinit var fakeRepository: FakeRepository

    @Mock
    private lateinit var firebaseReference: DatabaseReference

    @Mock
    private lateinit var dataAccess: RemoteDatabase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        fakeRepository = FakeRepository()
        apiInterface = Mockito.mock(ApiInterface::class.java)
        localData = Mockito.mock(PlayListDao::class.java)
        repositoryConcrete = RepositoryImpl(apiInterface, localData, dataAccess)
    }

    @Test
    fun `test repository to get data from remote server `() = runTest {
        val expectedData = SongDTO(id = 1, icon = "icon", title = "name", description = "url")
        `when`(apiInterface.getAllSong()).thenReturn(expectedData)

        val songs = repositoryConcrete.getDataFromServer()

        assertEquals(songs?.id, expectedData.id)
    }

    @Test
    fun `test local database to get data`() = runTest {
        val expectedData = Song(id = 1, icon = "icon", title = "name", description = "url")
        `when`(localData.getAllSongs()).thenReturn(expectedData)

        val songs = repositoryConcrete.getDataFromDatabase()

        assertEquals(songs?.id, expectedData.id)
    }

    @Test
    fun `test get song when rest api fails success Result`() = runTest {
        val expectedData = Song(id = 1, icon = "icon", title = "name", description = "url")
        `when`(localData.getAllSongs()).thenReturn(expectedData)
        `when`(apiInterface.getAllSong()).thenReturn(null)
        when (val actualData: Result<Song?> = repositoryConcrete.getSong()) {
            is Result.Success -> {
                assertEquals(actualData.data?.id, expectedData.id)
            }

            else -> {
                Assert.fail()
            }
        }
    }

    @Test
    fun `test get song when rest api fails failure Result`() = runTest {
        val expectedData = "No Internet Connection"
        when (val actualData: Result<String?> = fakeRepository.getSongFailure()) {
            is Result.Error -> {
                assertEquals(actualData.message, expectedData)
            }

            else -> {
                Assert.fail()
            }
        }
    }

    @Test
    fun `test write to firebaseRealtime`() =  runTest{

        //Arrange
        val song = Song(id = 1, icon = "icon", title = "name", description = "url")

        //Act
        repositoryConcrete.writeToFirebaseRealtimeDatabase(song)


        //Assert
        verify(dataAccess).writeDataToFirebaseRealtime(song)

    }
}
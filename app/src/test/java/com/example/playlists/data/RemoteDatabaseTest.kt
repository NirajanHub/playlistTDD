package com.example.playlists.data

import app.cash.turbine.test
import com.example.playlists.data.remote.RemoteDatabaseImpl
import com.example.playlists.domain.RemoteDatabase
import com.example.playlists.util.Output
import com.example.playlists.util.Result
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.whenever


class RemoteDatabaseTest {

    private lateinit var remoteDatabaseAccess: RemoteDatabase

    private lateinit var firebaseDatabase: FirebaseDatabase

    @Mock
    private lateinit var firebaseReference: DatabaseReference

    @Mock
    private lateinit var dataSnapshot: DataSnapshot

    @Mock
    private lateinit var databaseError: DatabaseError

    private val song = Song(
        id = 1,
        icon = "test_icon",
        title = "test_artist",
        description = "description"
    )

    @Before
    fun setUp() {

        MockitoAnnotations.openMocks(this)
        firebaseDatabase = Mockito.mock(FirebaseDatabase::class.java)
        `when`(firebaseDatabase.getReference("fake-server")).thenReturn(firebaseReference)
        remoteDatabaseAccess = RemoteDatabaseImpl(firebaseReference)
    }

    ////Testing getDataFromRealtimeDatabase function//////////////////
    @Test
    fun `test getDataFromFirebaseRealtime returns data`() = runTest {
        //Arrange
        // Example Song model
        `when`(dataSnapshot.getValue(Song::class.java)).thenReturn(song)

        //Act
        val listenerCaptor = argumentCaptor<ValueEventListener>()

        //Assert
        doAnswer {
            val listener = listenerCaptor.firstValue // why first . How many arguments does it contain ?
            listener.onDataChange(dataSnapshot)
            null
        }.`when`(firebaseReference).addListenerForSingleValueEvent(listenerCaptor.capture())

        remoteDatabaseAccess.getDataFromFirebaseRealtime().test {
            val result = awaitItem()
            assertEquals(Result.Success(song),result)
            cancelAndIgnoreRemainingEvents()
        }

       verify(firebaseReference, times(1)).addListenerForSingleValueEvent(any())
    }

    @Test
    fun `test getDataFromFirebaseRealtime returns failure`() = runTest{

       // `when`(dataSnapshot.getValue(Song::class.java)).thenReturn(null)
        val argumentCaptor = argumentCaptor<ValueEventListener>()

        doAnswer {
            val mockError = DatabaseError.fromCode(DatabaseError.PERMISSION_DENIED)
            argumentCaptor.firstValue.onCancelled(mockError)
            null
        }.`when`(firebaseReference).addListenerForSingleValueEvent(argumentCaptor.capture())

        remoteDatabaseAccess.getDataFromFirebaseRealtime().test {
            val result = awaitItem()
            assert(result is Result.Error)
            assertEquals("This client does not have permission to perform this operation",(result as Result.Error).message)
            cancelAndIgnoreRemainingEvents()
        }
        verify(firebaseReference, times(1)).addListenerForSingleValueEvent(any())
    }

    ///////////////////////////////////////    ///////////////////////////////////////     ///////////////////////////////////////


    ///////Testing write data to realtime firebase function//////
    @Test
    fun `test writeDataToFirebaseRealtime success`() = runTest {
        //Arrange

        val task : Task<Void> = mock()

        `when`(firebaseReference.child(anyString())).thenReturn(firebaseReference) // Mock the child reference
        `when`(firebaseReference.setValue(any())).thenReturn(task) // Mock the child reference

        //Act
        whenever(task.addOnSuccessListener(any())).thenAnswer{ invocation ->
            val successListener = invocation.arguments[0] as OnSuccessListener<*>
            successListener.onSuccess(null)
            task
        }

        //Assert
        remoteDatabaseAccess.writeDataToFirebaseRealtime(song).test {
            val value = awaitItem()
            assertEquals(value,Output.SUCCESS)
            cancelAndIgnoreRemainingEvents()
            verify(firebaseReference.child(anyString())).setValue(any())
        }



    }

    @Test
    fun `test writeDataToFirebaseRealtime failure`() = runTest{
        //Arrange
        val task : Task<Void> = mock()
        whenever(firebaseReference.child(anyString())).thenReturn(firebaseReference)
        whenever(firebaseReference.setValue(any())).thenReturn(task)
        whenever(firebaseReference.setValue(song).addOnSuccessListener(any())).thenReturn(task)


        doAnswer { invocation ->
            val onFailureListener = invocation.getArgument<OnFailureListener>(0)
            onFailureListener.onFailure(Exception("Failure"))
            task
        }.whenever(task).addOnFailureListener(any())


        //Ask
       remoteDatabaseAccess.writeDataToFirebaseRealtime(song).test {
           //Assert
           val value = awaitItem()
           assertEquals(Output.FAILURE, value)

           cancelAndIgnoreRemainingEvents()
           verify(firebaseReference.child(anyString()).setValue(song), times(2))
       }
    }

    @After
    fun tearDown() {
        Mockito.framework().clearInlineMocks()
    }

}



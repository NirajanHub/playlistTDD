package com.example.playlists.data

import com.example.playlists.domain.DataAccess
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations


class DataAccessTest {

    private lateinit var dataAccess: DataAccess

    private lateinit var firebaseDatabase: FirebaseDatabase

    @Mock
    private lateinit var firebaseReference: DatabaseReference

    @Mock
    private lateinit var childReference: DatabaseReference // Mock for the child node

    @Mock
    private lateinit var dataSnapshot: DataSnapshot

    @Mock
    private lateinit var databaseError: DatabaseError


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        firebaseDatabase = Mockito.mock(FirebaseDatabase::class.java)
        `when`(firebaseDatabase.getReference("fake-server")).thenReturn(firebaseReference)
        dataAccess = DataAccessImpl(firebaseReference)
    }

    @Test
    fun `test getDataFromFirebaseRealtime returns data`() {
        //Arrange
        val song = Song(
            id = 1,
            icon = "test_icon",
            title = "test_artist",
            description = "description"
        ) // Example Song model
        `when`(dataSnapshot.getValue(Song::class.java)).thenReturn(song)

        //Act

        val argumentCaptor = ArgumentCaptor.forClass(ValueEventListener::class.java)

        //Assert
        dataAccess.getDataFromFirebaseRealtime { result ->
            assert(result == song)
        }

        verify(firebaseReference).addListenerForSingleValueEvent(argumentCaptor.capture())

        argumentCaptor.value.onDataChange(dataSnapshot)
    }

    @Test
    fun `test getDataFromFirebaseRealtime returns null`(){

        `when`(dataSnapshot.getValue(Song::class.java)).thenReturn(null)

        val argumentCaptor = ArgumentCaptor.forClass(ValueEventListener::class.java)

        dataAccess.getDataFromFirebaseRealtime { result ->
            assert(result == null)
        }

        verify(firebaseReference).addListenerForSingleValueEvent(argumentCaptor.capture())
        argumentCaptor.value.onCancelled(databaseError)
    }

    @Test
    fun `test writeDataToFirebaseRealtime`() {
        //Arrange
        val song = Song(
            id = 1,
            icon = "test_icon",
            title = "test_artist",
            description = "description")
        `when`(firebaseReference.child(anyString())).thenReturn(childReference) // Mock the child reference


        //Act
        dataAccess.writeDataToFirebaseRealtime(song)

        //Assert
        val argumentCaptor = ArgumentCaptor.forClass(Song::class.java)
        verify(childReference).setValue(argumentCaptor.capture())
        assert(argumentCaptor.value == song)
    }
}



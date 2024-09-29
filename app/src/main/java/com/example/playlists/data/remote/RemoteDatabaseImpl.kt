package com.example.playlists.data.remote

import com.example.playlists.data.Song
import com.example.playlists.domain.RemoteDatabase
import com.example.playlists.util.Output
import com.example.playlists.util.Result
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RemoteDatabaseImpl @Inject constructor(private val firebaseReference: DatabaseReference) :
    RemoteDatabase {
    override fun getDataFromFirebaseRealtime(): Flow<Result<Song>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val model = dataSnapshot.getValue(Song::class.java)
                trySend(Result.Success(model))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Result.Error(error.message))
            }

        }
        firebaseReference.addListenerForSingleValueEvent(listener)
    }

    override fun writeDataToFirebaseRealtime(song: Song) : Flow<Output> = callbackFlow{
        val ref = firebaseReference.child("newdata").setValue(song)
        ref.addOnSuccessListener {
            trySend(Output.SUCCESS)
        }.addOnFailureListener {
            trySend(Output.FAILURE)
        }
    }
}
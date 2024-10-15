package com.example.playlists.mainplayer.data.remote

import android.util.Log
import com.example.playlists.mainplayer.data.Song
import com.example.playlists.mainplayer.domain.RemoteDatabase
import com.example.playlists.util.Output
import com.example.playlists.util.Result
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.json.JSONObject
import javax.inject.Inject

class RemoteDatabaseImpl @Inject constructor(private val firebaseReference: DatabaseReference) :
    RemoteDatabase {
    override fun getDataFromFirebaseRealtime(): Flow<Result<Song>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val key = dataSnapshot.child("newdata").child("key")
                val model = key.getValue(Song::class.java)
                Log.d("Nirajan",model.toString())
                trySend(Result.Success(model))
                //awaitCancellation()
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Result.Error(error.message))
            }
        }
        try {
            firebaseReference.addListenerForSingleValueEvent(listener)
            awaitCancellation()
        } catch (e: Exception) {
            awaitCancellation()
        }
    }

    override fun writeDataToFirebaseRealtime(song: Song): Flow<Output> = callbackFlow {
        val ref = firebaseReference.child("newdata").setValue(song)
        ref.addOnSuccessListener {
            trySend(Output.SUCCESS)
        }.addOnFailureListener {
            trySend(Output.FAILURE)
        }
    }
}
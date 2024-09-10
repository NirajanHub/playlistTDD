package com.example.playlists.data

import com.example.playlists.domain.DataAccess
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class DataAccessImpl @Inject constructor(private val firebaseReference: DatabaseReference) : DataAccess {

    override fun getDataFromFirebaseRealtime(callback: (Song?) -> Unit) {
        firebaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val model = dataSnapshot.getValue(Song::class.java)
                callback(model)
            }
            override fun onCancelled(error: DatabaseError) {
                callback(null)
            }
        })
    }

    override fun writeDataToFirebaseRealtime(song: Song) {
        firebaseReference.child("quiz").setValue(song)
    }


}
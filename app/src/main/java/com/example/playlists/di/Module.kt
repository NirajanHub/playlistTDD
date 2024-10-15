package com.example.playlists.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.playlists.mainplayer.data.PlayListDatabase
import com.example.playlists.mainplayer.data.remote.RemoteDatabaseImpl
import com.example.playlists.mainplayer.data.repository.RepositoryImpl
import com.example.playlists.mainplayer.domain.RemoteDatabase
import com.example.playlists.mainplayer.domain.Repository
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideFirebaseRealtimeDatabase(app: Application): FirebaseDatabase {
        val context: Context = app.applicationContext
        if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(context)
        }
        val firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseDatabase.setPersistenceEnabled(false)
        return firebaseDatabase
    }

//    @Provides
//    @Singleton
//    fun provideApiInterface(): ApiInterface{
//        return Retrofit.Builder()
//            .baseUrl("https://mocki.io/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(ApiInterface::class.java)
//    }


    @Provides
    @Singleton
    fun provideDataBase(app:Application): PlayListDatabase {
        return Room.databaseBuilder(
            app,
            PlayListDatabase::class.java,
            "playlist_db"
        ).build()
    }


    //Provides Remote Database for provideRepository function.
    @Provides
    @Singleton
    fun provideDataAccess(firebaseDatabase: FirebaseDatabase): RemoteDatabase {
        return RemoteDatabaseImpl(firebaseDatabase.getReference())
    }


    @Provides
    @Singleton
    fun provideRepository(db: PlayListDatabase, dataAccess: RemoteDatabase): Repository {
        return RepositoryImpl(db.dao(),dataAccess)
    }
}
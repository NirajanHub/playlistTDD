package com.example.playlists.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.playlists.data.PlayListDao
import com.example.playlists.data.PlayListDatabase
import com.example.playlists.data.repository.RepositoryImpl
import com.example.playlists.domain.ApiInterface
import com.example.playlists.domain.DataAccess
import com.example.playlists.domain.Repository
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideFirebaseRealtimeDatabase(): FirebaseDatabase {
        val context: Context = ApplicationProvider.getApplicationContext()
        if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(context)
        }
        val firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseDatabase.useEmulator("10.0.2.2",9000)
        firebaseDatabase.setPersistenceEnabled(false)
        return firebaseDatabase
    }

    @Provides
    @Singleton
    fun provideApiInterface(): ApiInterface{
        return Retrofit.Builder()
            .baseUrl("https://mocki.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }


    @Provides
    @Singleton
    fun provideDataBase(app:Application): PlayListDatabase{
        return Room.databaseBuilder(
            app,
            PlayListDatabase::class.java,
            "playlist_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDataAccess(firebaseDatabase: FirebaseDatabase): DataAccess{
        return com.example.playlists.data.DataAccessImpl(firebaseDatabase.reference)
    }


    @Provides
    @Singleton
    fun provideRepository(apiInterface: ApiInterface,db:PlayListDatabase,dataAccess: DataAccess): Repository{
        return RepositoryImpl(apiInterface,db.dao(),dataAccess)
    }
}
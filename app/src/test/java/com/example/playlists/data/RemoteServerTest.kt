package com.example.playlists.data

import com.example.playlists.domain.ApiInterface
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class RemoteServerTest {

    private lateinit var mockServer: MockWebServer
    private lateinit var api: ApiInterface

    @Before
    fun setUp() {
        mockServer = MockWebServer()
        mockServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockServer.url("/"))
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(ApiInterface::class.java)
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }

//    @Test
//    fun `test success api call`() {
//        //Act
//        runBlocking {
//            val mockResponse = MockResponse()
//                .setResponseCode(200)
//                .setBody("""{"id": 1,"icon":"icon", "title": "Test title", "description": "Test Description"}""")
//            mockServer.enqueue(mockResponse)
//
//            //
//            val response = api.getAllSong()
//
//            //Assert
//            assertEquals(1, response?.id)
//            assertEquals("Test title", response?.title)
//        }
//
//    }

//    @Test
//    fun `test failure api call`() {
//        //Act
//        runBlocking {
//            val mockResponse = MockResponse()
//                .setResponseCode(400)
//            mockServer.enqueue(mockResponse)
//
//            //
//            try {
//                val response = api.getAllSong()
//                assert(false)
//            }catch (e: HttpException) {
//                assert(true)
//            }
//        }
//
//    }
}
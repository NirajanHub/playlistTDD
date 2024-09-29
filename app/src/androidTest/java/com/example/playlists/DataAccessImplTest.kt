import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.playlists.MainCoroutineRule
import com.example.playlists.data.remote.RemoteDatabaseImpl
import com.example.playlists.domain.RemoteDatabase
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class FirebaseDatabaseInstrumentedTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var dataAccess: RemoteDatabase
    private lateinit var firebaseReference: DatabaseReference


    @Before
    fun setUp() {
        // Initialize Firebase for instrumented tests
        val context: Context = ApplicationProvider.getApplicationContext()
        if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(context)
        }
        // Initialize Firebase Realtime Database and use emulator
        firebaseDatabase = FirebaseDatabase.getInstance()
        //firebaseDatabase.useEmulator("127.0.0.1", 9000)  // Use the emulator for local testing
        firebaseDatabase.useEmulator(
            "10.0.2.2",
            9000
        )  // Use the emulator for local testing with physical device or emulator
        firebaseDatabase.setPersistenceEnabled(false)
        // Disable offline persistence for tests
        firebaseReference = firebaseDatabase.getReference("playlist")
        dataAccess = RemoteDatabaseImpl(firebaseReference = firebaseReference)
    }

    @Test
    fun testFirebaseDatabase_writeAndRead_success() =
        mainCoroutineRule.testScope.runTest {
            // Reference to your database location
            val ref = firebaseDatabase.getReference("playlist")
            // Write data to the database using coroutines
            //Log.d("Test", "Writing data to Firebase")
            ref.setValue("abc").addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    ref.get().addOnSuccessListener { dataSnapshot ->
                        //Log.d("Test", "Reading data from Firebase")
                        val value = dataSnapshot.getValue(String::class.java)
                        // Assert that the read value matches the written value
                        assertEquals("abc", value)
                    }
                } else {
                    Assert.fail()
                }
            }
        }

    @Test
    fun testFirebaseDatabase_writeAndRead_failure() =
        mainCoroutineRule.testScope.runTest {
            // Reference to your database location
            val ref = firebaseDatabase.reference

            // Write data to the database using coroutines
            //Log.d("Test", "Writing data to Firebase")
            ref.setValue("abc").addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    ref.get().addOnSuccessListener { dataSnapshot ->
                        //Log.d("Test", "Reading data from Firebase")
                        val value = dataSnapshot.getValue(String::class.java)
                        // Assert that the read value matches the written value
                        assertNotEquals("def", value)
                    }
                } else {
                    Assert.fail()
                }
            }
        }
}

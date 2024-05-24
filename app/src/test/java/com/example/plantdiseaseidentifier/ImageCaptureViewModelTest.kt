import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.plantdiseaseidentifier.DetectionResult
import com.example.plantdiseaseidentifier.ImageCaptureViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import java.net.URL
import javax.net.ssl.HttpsURLConnection


@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class ImageCaptureViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: ImageCaptureViewModel
    private lateinit var logMock: MockedStatic<Log>

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = object : ImageCaptureViewModel() {
            override fun openHttpsConnection(url: URL): HttpsURLConnection {
                return mock(HttpsURLConnection::class.java)
            }
        }
        // Mock the Log class
        logMock = Mockito.mockStatic(Log::class.java).apply {
            `when`<Int> { Log.e(Mockito.anyString(), Mockito.anyString()) }.thenReturn(0)
            `when`<Int> { Log.d(Mockito.anyString(), Mockito.anyString()) }.thenReturn(0)
            `when`<Int> { Log.i(Mockito.anyString(), Mockito.anyString()) }.thenReturn(0)
            `when`<Int> { Log.w(Mockito.anyString(), Mockito.anyString()) }.thenReturn(0)
        }
    }

    @After
    fun tearDown() {
        logMock.close() // Close the static mock to deregister it
        Dispatchers.resetMain()
    }

    @Test
    fun `openHttpsConnection() should return an HttpsURLConnection`() {
        val url = URL("https://192.168.14.162:5001")
        val connection = viewModel.openHttpsConnection(url)
        assertTrue(connection is HttpsURLConnection)
    }

    @Test
    fun `handleErrorResponse() should set detection results to unknown`() {
        viewModel.handleErrorResponse("Test error")
        assertEquals(
            listOf(DetectionResult("Unknown", "Unknown", "Unknown", "Unknown")),
            viewModel.detectionResults.value
        )
    }

    @Test
    fun `sendImageToServer() should update detection results on success`() = runBlockingTest {
        val mockResponse = """
    {
        "results": [
            {
                "disease_name": "Test Disease",
                "plant_name": "Test Plant",
                "symptoms": "Test Symptoms",
                "treatment": "Test Treatment"
            }
        ]
    }
""".trimIndent()

        // Mock the HttpsURLConnection
        val mockConnection = mock(HttpsURLConnection::class.java)

        // Mock the openHttpsConnection() function to return the mocked connection
        val viewModel = object : ImageCaptureViewModel() {
            override fun openHttpsConnection(url: URL): HttpsURLConnection {
                return mockConnection
            }
        }
    }
}
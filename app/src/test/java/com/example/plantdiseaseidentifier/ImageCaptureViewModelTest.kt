import android.content.Context
import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.*
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import com.example.plantdiseaseidentifier.DetectionResult
import com.example.plantdiseaseidentifier.ImageCaptureViewModel
import org.junit.Assert.*

@ExperimentalCoroutinesApi
class ImageCaptureViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `sendImageToServer successful response`() = testScope.runTest {
        val viewModel = ImageCaptureViewModel(mockWebServer.url("/").toString())

        // Mock context and bitmap
        val context = mock(Context::class.java)
        val bitmap = mock(Bitmap::class.java)

        // Simulate a successful server response
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("""
                {
                    "results": [
                        {
                            "disease_name": "Mock Disease",
                            "plant_name": "Mock Plant",
                            "symptoms": "Mock Symptoms",
                            "treatment": "Mock Treatment"
                        }
                    ]
                }
            """.trimIndent())
        mockWebServer.enqueue(mockResponse)

        viewModel.sendImageToServer(context, bitmap)

        // Observe the state flow and assert the results
        viewModel.detectionResults.collect {
            assertNotNull(it)
            assertTrue(it!!.isNotEmpty())
            assertEquals("Mock Disease", it[0].diseaseName)
        }
    }

    @Test
    fun `sendImageToServer error response`() = testScope.runTest {
        val viewModel = ImageCaptureViewModel(mockWebServer.url("/").toString())

        // Mock context and bitmap
        val context = mock(Context::class.java)
        val bitmap = mock(Bitmap::class.java)

        // Simulate an error server response
        val mockResponse = MockResponse().setResponseCode(500)
        mockWebServer.enqueue(mockResponse)

        viewModel.sendImageToServer(context, bitmap)

        // Observe the state flow and assert the error handling
        viewModel.detectionResults.collect {
            assertNotNull(it)
            assertTrue(it!!.isNotEmpty())
            assertEquals("Unknown", it[0].diseaseName)
        }
    }
}

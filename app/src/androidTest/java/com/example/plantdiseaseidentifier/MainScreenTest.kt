import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.plantdiseaseidentifier.DetectionResult
import com.example.plantdiseaseidentifier.ImageCaptureViewModel
import com.example.plantdiseaseidentifier.MainActivity
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class MainScreenIntegrationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testMainScreenToResultsScreenNavigation() {
        // Mock ViewModel
        val viewModel = mock(ImageCaptureViewModel::class.java)

        // Mock detection result
        val detectionResult = DetectionResult(
            diseaseName = "Disease1",
            plantName = "Plant1",
            symptoms = "Symptoms1",
            treatment = "Treatment1"
        )
        val detectionResultFlow = MutableStateFlow(listOf(detectionResult))
        `when`(viewModel.detectionResults).thenReturn(detectionResultFlow)

        // Wait for the component to appear
        composeTestRule.onNodeWithText("Click Below To Start Analyzing").assertExists()

        // Perform click action
        composeTestRule.onNodeWithText("Click Below To Start Analyzing").performClick()

        // Verify that ResultsScreen is displayed
        composeTestRule.onNodeWithText("Disease1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Plant1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Symptoms1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Treatment1").assertIsDisplayed()
    }
}

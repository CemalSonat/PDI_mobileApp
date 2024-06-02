/*package com.example.plantdiseaseidentifier

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class ResultsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var viewModel: ImageCaptureViewModel

    @Before
    fun setup() {
        viewModel = mockViewModel(DetectionResult("Disease 1", "Plant 1", "Symptoms 1", "Treatment 1"))
        composeTestRule.setContent {
            ResultsScreen(viewModel = viewModel, navController = rememberNavController())
        }
    }

    @Test
    fun resultsScreenDisplaysCorrectly() {
        // Verify that the result is displayed correctly
        composeTestRule.onNodeWithText("Disease 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Plant 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Symptoms 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Treatment 1").assertIsDisplayed()
    }

    @Test
    fun resultsScreenDisplaysUnknown() {
        // Change the detection result to "Unknown"
        `when`(viewModel.detectionResults).thenReturn(MutableStateFlow(listOf(
            DetectionResult("Unknown", "Unknown", "Unknown", "Unknown")
        )))

        // Verify that "unknown" values are displayed
        composeTestRule.onNodeWithText("Disease Name: Unknown").assertIsDisplayed()
        composeTestRule.onNodeWithText("Plant Name: Unknown").assertIsDisplayed()
        composeTestRule.onNodeWithText("Symptoms: Unknown").assertIsDisplayed()
        composeTestRule.onNodeWithText("Treatment: Unknown").assertIsDisplayed()
    }

    private fun mockViewModel(detectionResult: DetectionResult): ImageCaptureViewModel {
        val viewModel = mock(ImageCaptureViewModel::class.java)
        val stateFlow = MutableStateFlow(listOf(detectionResult))
        `when`(viewModel.detectionResults).thenReturn(stateFlow)
        return viewModel
    }
}
*/
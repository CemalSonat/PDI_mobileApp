/*package com.example.plantdiseaseidentifier

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.plantdiseaseidentifier.MainActivity
import com.example.plantdiseaseidentifier.ResultsScreen
import com.example.plantdiseaseidentifier.ImageCaptureViewModel
import com.example.plantdiseaseidentifier.DetectionResult
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import kotlinx.coroutines.flow.MutableStateFlow

class ResultsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun resultsScreen_showsLoading() {
        val viewModel = Mockito.mock(ImageCaptureViewModel::class.java).apply {
            Mockito.`when`(detectionResults).thenReturn(MutableStateFlow(null))
        }

        val navController = rememberNavController()

        composeTestRule.setContent {
            ResultsScreen(viewModel = viewModel, navController = navController)
        }

        // Check if the loading indicator is displayed
        composeTestRule.onNodeWithText("Loading...").assertIsDisplayed()
    }

    @Test
    fun resultsScreen_showsResults() {
        val mockResults = listOf(
            DetectionResult("Disease1", "Plant1", "Symptoms1", "Treatment1"),
            DetectionResult("Disease2", "Plant2", "Symptoms2", "Treatment2")
        )
        val viewModel = Mockito.mock(ImageCaptureViewModel::class.java).apply {
            Mockito.`when`(detectionResults).thenReturn(MutableStateFlow(mockResults))
        }

        val navController = rememberNavController()

        composeTestRule.setContent {
            ResultsScreen(viewModel = viewModel, navController = navController)
        }

        // Check if the results are displayed
        composeTestRule.onNodeWithText("Disease1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Plant1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Symptoms1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Treatment1").assertIsDisplayed()

        composeTestRule.onNodeWithText("Disease2").assertIsDisplayed()
        composeTestRule.onNodeWithText("Plant2").assertIsDisplayed()
        composeTestRule.onNodeWithText("Symptoms2").assertIsDisplayed()
        composeTestRule.onNodeWithText("Treatment2").assertIsDisplayed()
    }
}
*/
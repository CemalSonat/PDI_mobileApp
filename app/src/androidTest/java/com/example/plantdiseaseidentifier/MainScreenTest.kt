/*package com.example.plantdiseaseidentifier

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.plantdiseaseidentifier.MainActivity
import com.example.plantdiseaseidentifier.MainScreen
import com.example.plantdiseaseidentifier.ImageCaptureViewModel
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class MainScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun mainScreen_showsInitialText() {
        val navController = rememberNavController()
        val viewModel = mock(ImageCaptureViewModel::class.java)

        composeTestRule.setContent {
            MainScreen(navController = navController, viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Click Below To Start Analyzing")
            .assertIsDisplayed()
    }
*/
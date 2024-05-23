/*package com.example.plantdiseaseidentifier

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import com.example.plantdiseaseidentifier.MainActivity
import org.junit.Rule
import org.junit.Test

class WelcomeScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun welcomeScreen_displaysCorrectText() {
        composeTestRule.setContent {
            WelcomeScreen(onTimeout = {})
        }

        // Check if the text is displayed correctly
        composeTestRule.onNodeWithText("Plant Disease Identifier")
            .assertIsDisplayed()
    }
}
*/
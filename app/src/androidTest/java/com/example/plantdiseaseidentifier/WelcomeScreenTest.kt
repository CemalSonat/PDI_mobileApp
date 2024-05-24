package com.example.plantdiseaseidentifier

import WelcomeScreen
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import org.junit.Rule
import org.junit.Test

class WelcomeScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<TestActivity>()

    @Test
    fun welcomeScreen_displaysCorrectText() {
        composeTestRule.setContent {
            WelcomeScreen(onTimeout = {})
        }

        // Check if the text is displayed correctly
        composeTestRule.onNodeWithText("Plant Disease Identifier")
            .assertIsDisplayed()
    }

    @Test
    fun welcomeScreen_displaysErrorText() {
        composeTestRule.setContent {
            WelcomeScreen(onTimeout = {}, isError = true)
        }

        // Check if the error text is displayed correctly
        composeTestRule.onNodeWithText("An error occurred")
            .assertIsDisplayed()
    }
}
package com.example.plantdiseaseidentifier

import WelcomeScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlantDiseaseIdentifierApp()
        }
    }
}

@Composable
fun PlantDiseaseIdentifierApp() {
    val navController = rememberNavController()
    val viewModel: ImageCaptureViewModel = viewModel()

    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") {
            WelcomeScreen(onTimeout = {
                navController.navigate("main")
            })
        }
        composable("main") {
            MainScreen(navController, viewModel)
        }
        composable("results") {
            ResultsScreen(viewModel, navController)
        }
    }
}

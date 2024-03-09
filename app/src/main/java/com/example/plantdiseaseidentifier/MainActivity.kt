package com.example.plantdiseaseidentifier

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberImagePainter
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.material3.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException
import java.io.IOException


data class DetectionResult(val result: Boolean, val details: String)

class MainActivity : ComponentActivity() {

    // Function for requesting camera permissions
    private val cameraPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            // Check if the camera permission is granted
            if (isGranted) {
                // If permission is granted, show the Compose content
                showComposeContent()
            } else {
                // If permission is denied, display a toast message
                Toast.makeText(this, "Camera permission is required to capture images.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    // initiate the display of the UI of the application
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showComposeContent()
    }
    // Initialize the main content of the activity
    private fun showComposeContent() {
        setContent {
            ImageCaptureScreen(context = this)
        }
    }
}

// Main screen for capturing or choosing images
@Composable
fun ImageCaptureScreen(context: Context = LocalContext.current) {
    // Creating a Temporary Image File and Obtaining URI
    val file = LocalContext.current.createImageFile()
    val uri = FileProvider.getUriForFile(
        LocalContext.current,
        "${LocalContext.current.packageName}.provider",
        file
    )

    // remember and mutableStateOf ensure that changes to these variables trigger recomposition of the UI
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    val cameraPermissionGranted = remember { mutableStateOf(false) }

    // ViewModel to handle business logic
    val viewModel = remember { ImageCaptureViewModel() }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccessful: Boolean ->
        if (isSuccessful) {
            // Access the image URI using the URI passed to the camera launcher
            val uri = uri
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                viewModel.sendImageToServer(bitmap)
            } catch (e: FileNotFoundException) {
                // Handle file not found
                Toast.makeText(context, "Error: Unable to load captured image.", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Handle the case where the picture was not taken successfully
            Toast.makeText(context, "Error: Unable to capture image.", Toast.LENGTH_SHORT).show()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        onPermissionResult(it, context, cameraLauncher, uri, cameraPermissionGranted)
    }

    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let { uri ->
            capturedImageUri = uri  // Update capturedImageUri with the selected image URI
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                viewModel.sendImageToServer(bitmap)
            } catch (e: FileNotFoundException) {
                // Handle file not found
                Toast.makeText(context, "Error: Unable to load selected image.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    // Checks whether the permission is already granted
    val requestCameraPermission: () -> Unit = {
        val permissionCheckResult =
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
            cameraPermissionGranted.value = true
            cameraLauncher.launch(uri)
        } else {
            // Request permission if not granted
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Observe the result from ViewModel
    val isDiseaseDetected by viewModel.isDiseaseDetected.collectAsState()

    // Compose UI Components
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val backgroundImage = painterResource(id = R.drawable.plant) // Replace with your image resource

        // Draw the background image with content
        Image(
            painter = backgroundImage,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.FillHeight
        )

        // Two buttons on top of the image
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Capture Image Button
            Button(
                onClick = {
                    requestCameraPermission()
                },
                modifier = Modifier
                    .padding(40.dp)
                    .fillMaxWidth()
                    .height(80.dp),
                contentPadding = PaddingValues(16.dp),
            ) {
                Text(text = "Capture Image", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Choose Photo Button
            Button(
                onClick = {
                    pickImageLauncher.launch("image/*")
                },
                modifier = Modifier
                    .padding(40.dp)
                    .fillMaxWidth()
                    .height(80.dp),
                contentPadding = PaddingValues(16.dp),
            ) {
                Text(text = "Choose Photo", fontSize = 18.sp)
            }

            // Display result from ViewModel
            isDiseaseDetected?.let { result ->
                // Show the result in a dialog
                ResultDialog(result = result) {
                    // You can perform any action when the dialog is closed, if needed
                }
            }
        }
    }
}


// Handles the result of a camera permission request
fun onPermissionResult(
    granted: Boolean,
    context: Context,
    cameraLauncher: ActivityResultLauncher<Uri>,
    uri: Uri,
    cameraPermissionGranted: MutableState<Boolean>
) {
    if (granted) {
        Toast.makeText(context, R.string.permission_granted, Toast.LENGTH_SHORT).show()
        cameraPermissionGranted.value = true
        cameraLauncher.launch(uri)
    } else {
        Toast.makeText(context, R.string.permission_denied, Toast.LENGTH_SHORT).show()
    }
}

fun onImageSelected(selectedImageUri: Uri, capturedImageUri: Uri?) {
    // Handle image here
}

// Creates a temporary image file with a unique name based on the current timestamp
fun Context.createImageFile(): File {
    // Generate a timestamp to create a unique image file name
    val timeStamp = SimpleDateFormat("yyyy_MM_dd_HH:mm:ss").format(Date())
    // Concatenate the timestamp with a standard file name format
    val imageFileName = "JPEG_$timeStamp.jpg"
    // Create a temporary image file with the specified name and extension
    val image = File.createTempFile(
        imageFileName,
        null, // The file extension is already part of the imageFileName
        // Store the file in the external cache directory
        externalCacheDir
    )

    return image
}

class ImageCaptureViewModel : ViewModel() {
    private val _isDiseaseDetected = MutableStateFlow<Boolean?>(null)
    val isDiseaseDetected: StateFlow<Boolean?> = _isDiseaseDetected

    fun sendImageToServer(imageBitmap: Bitmap) {
        viewModelScope.launch {
            try {
                val url = URL("http://192.168.0.104:5000/predict")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doInput = true
                connection.doOutput = true

                connection.setRequestProperty("Content-Type", "application/json")

                val byteArrayOutputStream = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()
                val encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT)

                val jsonRequest = JSONObject().apply {
                    put("image", encodedImage)
                }

                val outputStream = connection.outputStream
                outputStream.write(jsonRequest.toString().toByteArray())
                outputStream.close()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val resultJson = inputStream.bufferedReader().use { it.readText() }

                    val result = Gson().fromJson(resultJson, DetectionResult::class.java)

                    inputStream.close()

                    _isDiseaseDetected.value = result.result
                } else {
                    // Handle HTTP error codes
                    _isDiseaseDetected.value = false
                    Log.e("ImageCaptureViewModel", "HTTP Error: $responseCode")
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                _isDiseaseDetected.value = false
                Log.e("ImageCaptureViewModel", "Error: File not found.")
            } catch (e: IOException) {
                e.printStackTrace()
                _isDiseaseDetected.value = false
                Log.e("ImageCaptureViewModel", "Error: IO Exception.")
            } catch (e: Exception) {
                e.printStackTrace()
                _isDiseaseDetected.value = false
                Log.e("ImageCaptureViewModel", "Error: ${e.message}")
            }
        }
    }
}

// Preview function for ImageCaptureScreen
@Preview(showBackground = true)
@Composable
fun ImageCaptureScreenPreview() {
    MaterialTheme {
        Surface {
            // Use LocalContext.current for preview
            ImageCaptureScreen(context = LocalContext.current)
        }
    }
}

@Composable
fun ResultDialog(result: Boolean, onClose: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onClose() },
        title = { Text("Disease Detection Result") },
        text = { Text("Disease Detected: $result") },
        confirmButton = {
            Button(
                onClick = {
                    onClose()
                }
            ) {
                Text("OK")
            }
        }
    )
}


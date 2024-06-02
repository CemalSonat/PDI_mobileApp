package com.example.plantdiseaseidentifier

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MainScreen(navController: NavController, viewModel: ImageCaptureViewModel) {
    val context = LocalContext.current
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    val cameraPermissionGranted = remember { mutableStateOf(false) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccessful: Boolean ->
        if (isSuccessful) {
            capturedImageUri?.let { uri ->
                Log.d("CameraLauncher", "Image captured successfully: $uri")
                processCapturedImage(context, uri, viewModel)
                navController.navigate("results")
            }
        } else {
            Toast.makeText(context, "Error: Unable to capture image.", Toast.LENGTH_SHORT).show()
            Log.e("CameraLauncher", "Image capture failed.")
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        onPermissionResult(it, context, cameraLauncher, capturedImageUri, cameraPermissionGranted)
    }

    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            capturedImageUri = it
            Log.d("PickImageLauncher", "Image picked successfully: $uri")
            processCapturedImage(context, uri, viewModel)
            navController.navigate("results")
        }
    }

    val selectPhotosLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK && result.data != null) {
            val selectedPhotosUri = result.data?.data
            selectedPhotosUri?.let {
                capturedImageUri = it
                processCapturedImage(context, it, viewModel)
                navController.navigate("results")
            }
        }
    }

    val requestCameraPermission: () -> Unit = {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            cameraPermissionGranted.value = true
            capturedImageUri = createImageUri(context)
            capturedImageUri?.let { cameraLauncher.launch(it) }
        } else {
            permissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    val requestStoragePermission: () -> Unit = {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            }
            selectPhotosLauncher.launch(intent)
        } else {
            pickImageLauncher.launch("image/*")
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.plant7),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.8f)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        showBottomSheet(context, requestCameraPermission, requestStoragePermission)
                    }
                )
            }
        ) {
            Text(
                text = "Click Below To Start Analyzing",
                color = Color.Black,
                fontSize = 25.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.add_circle),
                contentDescription = "Custom Icon",
                modifier = Modifier.size(150.dp)
            )
        }
    }
}

fun showBottomSheet(context: Context, requestCameraPermission: () -> Unit, requestStoragePermission: () -> Unit) {
    val bottomSheetDialog = BottomSheetDialog(context)
    val bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_layout, null)
    bottomSheetView.findViewById<TextView>(R.id.takePhotoOption).setOnClickListener {
        requestCameraPermission()
        bottomSheetDialog.dismiss()
    }
    bottomSheetView.findViewById<TextView>(R.id.uploadPhotoOption).setOnClickListener {
        requestStoragePermission()
        bottomSheetDialog.dismiss()
    }
    bottomSheetDialog.setContentView(bottomSheetView)
    bottomSheetDialog.show()
}

fun processCapturedImage(context: Context, uri: Uri, viewModel: ImageCaptureViewModel) {
    try {
        Log.d("ProcessCapturedImage", "URI: $uri")

        val mimeType = context.contentResolver.getType(uri)
        Log.d("ProcessCapturedImage", "MIME type: $mimeType")

        val inputStream = context.contentResolver.openInputStream(uri)
        Log.d("ImageCapture", "Input stream opened successfully: $inputStream")

        if (inputStream != null) {
            val bitmap = BitmapFactory.decodeStream(inputStream)
            Log.d("ImageCapture", "Bitmap decoded successfully: $bitmap")
            inputStream.close()

            if (bitmap != null) {
                viewModel.sendImageToServer(context, bitmap)
                Log.d("ProcessCapturedImage", "Bitmap processed successfully")
            } else {
                Toast.makeText(context, "Error: Bitmap is null.", Toast.LENGTH_SHORT).show()
                Log.e("ProcessCapturedImage", "Bitmap is null.")
            }
        } else {
            Toast.makeText(context, "Error: Unable to open input stream.", Toast.LENGTH_SHORT).show()
            Log.e("ProcessCapturedImage", "Input stream is null.")
        }
    } catch (e: FileNotFoundException) {
        Toast.makeText(context, "Error: Unable to load image.", Toast.LENGTH_SHORT).show()
        Log.e("ProcessCapturedImage", "FileNotFoundException: ${e.message}", e)
    } catch (e: IOException) {
        Toast.makeText(context, "Error: Unable to decode image. ${e.message}", Toast.LENGTH_SHORT).show()
        Log.e("ProcessCapturedImage", "IOException: ${e.message}", e)
    } catch (e: Exception) {
        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        Log.e("ProcessCapturedImage", "Exception: ${e.message}", e)
    }
}

fun createImageUri(context: Context): Uri? {
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "JPEG_${SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US).format(Date())}.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/YourAppName")
    }

    return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
}

fun onPermissionResult(
    granted: Boolean,
    context: Context,
    cameraLauncher: ActivityResultLauncher<Uri>,
    uri: Uri?,
    cameraPermissionGranted: MutableState<Boolean>
) {
    if (granted) {
        Toast.makeText(context, "Permission granted", Toast.LENGTH_SHORT).show()
        cameraPermissionGranted.value = true
        uri?.let { cameraLauncher.launch(it) }
    } else {
        Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
    }
}


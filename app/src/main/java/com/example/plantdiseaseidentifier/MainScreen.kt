package com.example.plantdiseaseidentifier

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
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
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun MainScreen(navController: NavController, viewModel: ImageCaptureViewModel) {
    val context = LocalContext.current
    val file = createImageFile(context)
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    val cameraPermissionGranted = remember { mutableStateOf(false) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccessful: Boolean ->
        if (isSuccessful) {
            processCapturedImage(context, uri, viewModel)
            navController.navigate("results")
        } else {
            Toast.makeText(context, "Error: Unable to capture image.", Toast.LENGTH_SHORT).show()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        onPermissionResult(it, context, cameraLauncher, uri, cameraPermissionGranted)
    }

    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            capturedImageUri = uri
            processCapturedImage(context, uri, viewModel)
            navController.navigate("results")
        }
    }

    val requestCameraPermission: () -> Unit = {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            cameraPermissionGranted.value = true
            cameraLauncher.launch(uri)
        } else {
            permissionLauncher.launch(android.Manifest.permission.CAMERA)
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
                        showBottomSheet(context, requestCameraPermission, pickImageLauncher)
                    }
                )
            }
        ) {
            Text(
                text = "Click Below To Start Analyzing",
                color = Color.Black,
                fontSize = 25.sp,
                modifier = Modifier.padding(bottom = 16.dp) // Adjust padding as needed
            )
            Image(
                painter = painterResource(id = R.drawable.add_circle),
                contentDescription = "Custom Icon",
                modifier = Modifier.size(150.dp) // Adjust the size as per your requirement
            )
        }
    }
}

fun showBottomSheet(context: Context, requestCameraPermission: () -> Unit, pickImageLauncher: ActivityResultLauncher<String>) {
    val bottomSheetDialog = BottomSheetDialog(context)
    val bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_layout, null)
    bottomSheetView.findViewById<TextView>(R.id.takePhotoOption).setOnClickListener {
        requestCameraPermission()
        bottomSheetDialog.dismiss()
    }
    bottomSheetView.findViewById<TextView>(R.id.uploadPhotoOption).setOnClickListener {
        pickImageLauncher.launch("image/*")
        bottomSheetDialog.dismiss()
    }
    bottomSheetDialog.setContentView(bottomSheetView)
    bottomSheetDialog.show()
}

fun processCapturedImage(context: Context, uri: Uri, viewModel: ImageCaptureViewModel) {
    try {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            val bitmap = BitmapFactory.decodeStream(inputStream)
            viewModel.sendImageToServer(context, bitmap)
        }
    } catch (e: FileNotFoundException) {
        Toast.makeText(context, "Error: Unable to load image.", Toast.LENGTH_SHORT).show()
    }
}

fun createImageFile(context: Context): File {
    val timeStamp = SimpleDateFormat("yyyy_MM_dd_HH:mm:ss").format(Date())
    val imageFileName = "JPEG_$timeStamp.jpg"
    return File.createTempFile(imageFileName, null, context.externalCacheDir)
}

fun onPermissionResult(
    granted: Boolean,
    context: Context,
    cameraLauncher: ActivityResultLauncher<Uri>,
    uri: Uri,
    cameraPermissionGranted: MutableState<Boolean>
) {
    if (granted) {
        Toast.makeText(context, "Permission granted", Toast.LENGTH_SHORT).show()
        cameraPermissionGranted.value = true
        cameraLauncher.launch(uri)
    } else {
        Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
    }
}


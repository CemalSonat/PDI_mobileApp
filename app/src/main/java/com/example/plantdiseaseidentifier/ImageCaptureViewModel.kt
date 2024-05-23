package com.example.plantdiseaseidentifier

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.*

data class DetectionResult(
    val diseaseName: String,
    val plantName: String,
    val symptoms: String,
    val treatment: String
)

class ImageCaptureViewModel(private val baseUrl: String = "https://172.20.10.3:5001") : ViewModel() {
    private val _detectionResults = MutableStateFlow<List<DetectionResult>?>(null)
    val detectionResults: StateFlow<List<DetectionResult>?> = _detectionResults

    fun sendImageToServer(context: Context, imageBitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            var connection: HttpsURLConnection? = null
            var byteArrayOutputStream: ByteArrayOutputStream? = null

            try {
                val url = URL("$baseUrl/predict")
                Log.d("ImageCaptureViewModel", "Sending request to: $url")

                // Load your certificate from res/raw directory
                val certificateFactory = CertificateFactory.getInstance("X.509")
                val inputStream = context.resources.openRawResource(R.raw.cert)
                val certificate = certificateFactory.generateCertificate(inputStream)
                inputStream.close()

                // Create a KeyStore containing your certificate
                val keyStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
                    load(null, null)
                    setCertificateEntry("ca", certificate)
                }

                // Create a TrustManager that trusts the CAs in your KeyStore
                val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
                    init(keyStore)
                }
                val trustManagers = trustManagerFactory.trustManagers

                // Create an SSLContext that uses your TrustManager
                val sslContext = SSLContext.getInstance("TLS").apply {
                    init(null, trustManagers, null)
                }

                // Create a HostnameVerifier that bypasses hostname verification
                val hostnameVerifier = HostnameVerifier { _, _ -> true }

                connection = (url.openConnection() as HttpsURLConnection).apply {
                    sslSocketFactory = sslContext.socketFactory
                    setHostnameVerifier(hostnameVerifier) // Set the custom HostnameVerifier
                    requestMethod = "POST"
                    doInput = true
                    doOutput = true
                    setRequestProperty("Content-Type", "application/json")
                }

                byteArrayOutputStream = ByteArrayOutputStream().apply {
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, this)
                }

                val encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
                val jsonRequest = JSONObject().apply { put("image", encodedImage) }

                Log.d("ImageCaptureViewModel", "JSON request: $jsonRequest")

                connection.outputStream.use { it.write(jsonRequest.toString().toByteArray()) }

                val responseCode = connection.responseCode
                Log.d("ImageCaptureViewModel", "Response code: $responseCode")

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val resultJson = connection.inputStream.bufferedReader().use { it.readText() }
                    Log.d("ImageCaptureViewModel", "Response JSON: $resultJson")

                    val jsonObject = JSONObject(resultJson)
                    val resultsArray = jsonObject.getJSONArray("results")

                    val detectionResults = mutableListOf<DetectionResult>()
                    for (i in 0 until resultsArray.length()) {
                        val resultObject = resultsArray.getJSONObject(i)
                        val detectionResult = DetectionResult(
                            diseaseName = resultObject.getString("disease_name"),
                            plantName = resultObject.getString("plant_name"),
                            symptoms = resultObject.getString("symptoms"),
                            treatment = resultObject.getString("treatment")
                        )
                        detectionResults.add(detectionResult)
                    }

                    _detectionResults.value = detectionResults
                    Log.d("ImageCaptureViewModel", "Detection result: ${detectionResults.firstOrNull()}")
                } else {
                    handleErrorResponse("HTTP Error: $responseCode")
                }
            } catch (e: IOException) {
                handleErrorResponse("IO Exception: ${e.message}")
                Log.e("ImageCaptureViewModel", "Error: IO Exception.", e)
            } catch (e: Exception) {
                handleErrorResponse("Exception: ${e.message}")
                Log.e("ImageCaptureViewModel", "Error: ${e.message}", e)
            } finally {
                byteArrayOutputStream?.close()
                connection?.disconnect()
            }
        }
    }

    internal fun handleErrorResponse(message: String) {
        _detectionResults.value = listOf(
            DetectionResult("Unknown", "Unknown", "Unknown", "Unknown")
        )
        Log.e("ImageCaptureViewModel", message)
    }
}


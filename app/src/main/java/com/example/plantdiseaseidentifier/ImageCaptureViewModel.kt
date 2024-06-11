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
import java.io.FileNotFoundException
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

sealed class DetectionResult {
    data class Detailed(
        val diseaseName: String,
        val plantName: String,
        val symptoms: String,
        val treatment: String
    ) : DetectionResult()

    data class Simple(val result: String) : DetectionResult()
}

open class ImageCaptureViewModel(private val baseUrl: String = "https://172.20.10.3:5001") : ViewModel() {
    private val _detectionResults = MutableStateFlow<List<DetectionResult>?>(null)
    open val detectionResults: StateFlow<List<DetectionResult>?> = _detectionResults

    fun sendImageToServer(context: Context, imageBitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            // Clear previous results and show loading indicator
            _detectionResults.value = null

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

                connection = openHttpsConnection(url).apply {
                    sslSocketFactory = sslContext.socketFactory
                    setHostnameVerifier(hostnameVerifier) // Set the custom HostnameVerifier
                    requestMethod = "POST"
                    doInput = true
                    doOutput = true
                    setRequestProperty("Content-Type", "application/json")
                }

                byteArrayOutputStream = ByteArrayOutputStream().apply {
                    val mutableBitmap = imageBitmap.copy(Bitmap.Config.ARGB_8888, true)
                    val result = mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 100, this)
                    if (!result) {
                        Log.e("ImageCaptureViewModel", "Failed to compress bitmap")
                        return@launch
                    }
                }

                val encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
                val jsonRequest = JSONObject().apply { put("image", encodedImage) }

                Log.d("ImageCaptureViewModel", "JSON request: $jsonRequest")

                connection.outputStream.use { it.write(jsonRequest.toString().toByteArray()) }

                val responseCode = connection.responseCode
                Log.d("ImageCaptureViewModel", "Response code: $responseCode")

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try {
                        val resultJson = connection.inputStream.bufferedReader().use { it.readText() }
                        Log.d("ImageCaptureViewModel", "Response JSON: $resultJson")

                        val jsonObject = JSONObject(resultJson)
                        val resultsArray = jsonObject.getJSONArray("results")

                        val detectionResults = mutableListOf<DetectionResult>()

                        for (i in 0 until resultsArray.length()) {
                            val resultObject = resultsArray.getJSONObject(i)

                            val plantName = resultObject.optString("plant_name", "")
                            val diseaseName = resultObject.optString("disease_name", "healthy")

                            if (diseaseName == "healthy") {
                                val detectionResult = DetectionResult.Simple("$plantName is healthy")
                                detectionResults.add(detectionResult)
                                Log.d("ImageCaptureViewModel", "The plant is healthy. Plant name: $plantName")
                            } else {
                                val symptoms = resultObject.optString("symptoms", "No symptoms information available")
                                val treatment = resultObject.optString("treatment", "No treatment information available")
                                val detectionResult = DetectionResult.Detailed(diseaseName, plantName, symptoms, treatment)
                                detectionResults.add(detectionResult)
                                Log.d("ImageCaptureViewModel", "Disease detected. Plant name: $plantName, Disease name: $diseaseName, Symptoms: $symptoms, Treatment: $treatment")
                            }
                        }

                        _detectionResults.value = detectionResults
                        Log.d("ImageCaptureViewModel", "Detection result: ${detectionResults.firstOrNull()}")
                    } catch (e: FileNotFoundException) {
                        handleErrorResponse("File not found: ${e.message}")
                        Log.e("ImageCaptureViewModel", "FileNotFoundException.", e)
                    }
                } else {
                    handleErrorResponse("HTTP Error: $responseCode")
                }
            } catch (e: IOException) {
                handleErrorResponse("IO Exception: ${e.message}")
                Log.e("ImageCaptureViewModel", "IOException.", e)
            } catch (e: Exception) {
                handleErrorResponse("Exception: ${e.message}")
                Log.e("ImageCaptureViewModel", "Exception.", e)
            } finally {
                byteArrayOutputStream?.close()
                connection?.disconnect()
            }
        }
    }

    internal fun handleErrorResponse(message: String) {
        val defaultResult = DetectionResult.Simple("Unknown Plant")
        _detectionResults.value = listOf(defaultResult)
        Log.e("ImageCaptureViewModel", message)
    }

    open fun openHttpsConnection(url: URL): HttpsURLConnection {
        return url.openConnection() as HttpsURLConnection
    }
}
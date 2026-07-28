package com.example.executor.strategies

import android.content.Context
import android.content.ContentValues
import android.provider.MediaStore
import android.os.Environment
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CaptureRequest
import android.media.ImageReader
import android.graphics.ImageFormat
import android.os.Handler
import android.os.HandlerThread
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.io.File
import java.io.FileOutputStream
import com.example.executor.ShortcutExecutionCallbacks

object CameraCaptureHelper {
    suspend fun capturePhoto(
        context: Context,
        cameraType: String,
        saveDestination: String,
        callbacks: ShortcutExecutionCallbacks
    ): String = withContext(Dispatchers.IO) {
        if (androidx.core.app.ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            callbacks.onLog("   ❌ Error: Permiso de CÁMARA (android.permission.CAMERA) no concedido.")
            return@withContext "Error: Sin permiso de cámara"
        }

        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = try {
            val facing = if (cameraType == "FRONT") {
                CameraCharacteristics.LENS_FACING_FRONT
            } else {
                CameraCharacteristics.LENS_FACING_BACK
            }
            cameraManager.cameraIdList.firstOrNull { id ->
                val characteristics = cameraManager.getCameraCharacteristics(id)
                characteristics.get(CameraCharacteristics.LENS_FACING) == facing
            }
        } catch (e: Exception) {
            null
        }

        if (cameraId == null) {
            callbacks.onLog("   ❌ Error: No se encontró cámara disponible para tipo: $cameraType")
            return@withContext "Error: Cámara no encontrada"
        }

        val resultDeferred = CompletableDeferred<String>()
        val backgroundThread = HandlerThread("CameraBackground").apply { start() }
        val backgroundHandler = Handler(backgroundThread.looper)

        var cameraDevice: CameraDevice? = null
        var imageReader: ImageReader? = null
        var captureSession: android.hardware.camera2.CameraCaptureSession? = null

        try {
            // Pick a reasonable image resolution
            val size = try {
                val characteristics = cameraManager.getCameraCharacteristics(cameraId)
                val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                val sizes = map?.getOutputSizes(ImageFormat.JPEG)
                sizes?.firstOrNull { it.width <= 1920 && it.height <= 1080 } ?: sizes?.firstOrNull() ?: android.util.Size(1280, 720)
            } catch (e: Exception) {
                android.util.Size(1280, 720)
            }

            callbacks.onLog("   ⚙️ Configurando resolución de captura: ${size.width}x${size.height}")
            imageReader = ImageReader.newInstance(size.width, size.height, ImageFormat.JPEG, 2)
            
            imageReader.setOnImageAvailableListener({ reader ->
                val image = try { reader.acquireLatestImage() } catch (e: Exception) { null }
                if (image != null) {
                    try {
                        val buffer = image.planes[0].buffer
                        val bytes = ByteArray(buffer.remaining())
                        buffer.get(bytes)
                        
                        if (saveDestination == "GALLERY") {
                            val filename = "kinetix_photo_${System.currentTimeMillis()}.jpg"
                            val contentValues = ContentValues().apply {
                                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Kinetix")
                                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                                }
                            }

                            val resolver = context.contentResolver
                            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                            if (uri != null) {
                                try {
                                    resolver.openOutputStream(uri).use { out ->
                                        out?.write(bytes)
                                    }
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                                        contentValues.clear()
                                        contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                                        resolver.update(uri, contentValues, null, null)
                                    }
                                    val absolutePath = uri.toString()
                                    callbacks.onLog("   ✅ Foto guardada en la Galería Pública (Kinetix): $absolutePath")
                                    resultDeferred.complete(absolutePath)
                                } catch (e: Exception) {
                                    callbacks.onLog("   ❌ Error al escribir en Galería: ${e.localizedMessage}")
                                    val file = File(context.cacheDir, filename)
                                    FileOutputStream(file).use { out -> out.write(bytes) }
                                    resultDeferred.complete(file.absolutePath)
                                }
                            } else {
                                val file = File(context.cacheDir, filename)
                                FileOutputStream(file).use { out -> out.write(bytes) }
                                resultDeferred.complete(file.absolutePath)
                            }
                        } else {
                            val filename = "kinetix_secured_${System.currentTimeMillis()}.jpg"
                            val file = File(context.cacheDir, filename)
                            FileOutputStream(file).use { out -> out.write(bytes) }
                            val absolutePath = file.absolutePath
                            callbacks.onLog("   🔐 Foto guardada en apartado privado: $absolutePath")
                            resultDeferred.complete(absolutePath)
                        }
                    } catch (e: Exception) {
                        callbacks.onLog("   ❌ Error al guardar imagen: ${e.localizedMessage}")
                        resultDeferred.complete("Error al guardar imagen")
                    } finally {
                        image.close()
                    }
                } else {
                    resultDeferred.complete("Error: No se obtuvo imagen")
                }
            }, backgroundHandler)

            cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) {
                    cameraDevice = camera
                    try {
                        val targets = listOf(imageReader.surface)
                        camera.createCaptureSession(targets, object : android.hardware.camera2.CameraCaptureSession.StateCallback() {
                            override fun onConfigured(session: android.hardware.camera2.CameraCaptureSession) {
                                captureSession = session
                                try {
                                    val requestBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE).apply {
                                        addTarget(imageReader.surface)
                                        // Auto focus and auto exposure
                                        set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                                        set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)
                                    }
                                    
                                    // Start high-speed single capture
                                    session.capture(requestBuilder.build(), object : android.hardware.camera2.CameraCaptureSession.CaptureCallback() {
                                        override fun onCaptureCompleted(
                                            session: android.hardware.camera2.CameraCaptureSession,
                                            request: CaptureRequest,
                                            result: android.hardware.camera2.TotalCaptureResult
                                        ) {
                                            callbacks.onLog("   📸 Disparo de cámara ejecutado.")
                                        }

                                        override fun onCaptureFailed(
                                            session: android.hardware.camera2.CameraCaptureSession,
                                            request: CaptureRequest,
                                            failure: android.hardware.camera2.CaptureFailure
                                        ) {
                                            callbacks.onLog("   ❌ Disparo fallido: ${failure.reason}")
                                            resultDeferred.complete("Error de captura física")
                                        }
                                    }, backgroundHandler)
                                } catch (e: Exception) {
                                    callbacks.onLog("   ❌ Error al iniciar captura: ${e.localizedMessage}")
                                    resultDeferred.complete("Error de captura")
                                }
                            }

                            override fun onConfigureFailed(session: android.hardware.camera2.CameraCaptureSession) {
                                callbacks.onLog("   ❌ Configuración de sesión de cámara fallida.")
                                resultDeferred.complete("Error de configuración de sesión")
                            }
                        }, backgroundHandler)
                    } catch (e: Exception) {
                        callbacks.onLog("   ❌ Error al configurar sesión: ${e.localizedMessage}")
                        resultDeferred.complete("Error de sesión")
                    }
                }

                override fun onDisconnected(camera: CameraDevice) {
                    callbacks.onLog("   ⚠️ Cámara desconectada.")
                    resultDeferred.complete("Cámara desconectada")
                }

                override fun onError(camera: CameraDevice, error: Int) {
                    callbacks.onLog("   ❌ Error de cámara: $error")
                    resultDeferred.complete("Error de dispositivo de cámara: $error")
                }
            }, backgroundHandler)

            // Wait with a max timeout of 8 seconds
            val resultPath = withTimeout(8000L) {
                resultDeferred.await()
            }
            resultPath
        } catch (e: Exception) {
            callbacks.onLog("   ❌ Falló captura: ${e.localizedMessage}")
            "Error: ${e.localizedMessage}"
        } finally {
            // Clean up resources properly
            try { captureSession?.close() } catch (e: Exception) {}
            try { cameraDevice?.close() } catch (e: Exception) {}
            try { imageReader?.close() } catch (e: Exception) {}
            backgroundThread.quitSafely()
        }
    }
}

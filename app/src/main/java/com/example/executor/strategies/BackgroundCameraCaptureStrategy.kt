package com.example.executor.strategies

import android.content.Context
import android.speech.tts.TextToSpeech
import com.example.executor.ActionStrategy
import com.example.executor.ShortcutExecutionCallbacks

class BackgroundCameraCaptureStrategy : ActionStrategy {
    override suspend fun execute(
        context: Context,
        tts: TextToSpeech?,
        callbacks: ShortcutExecutionCallbacks,
        resolvedParams: Map<String, String>,
        currentInput: String
    ): String {
        val cameraType = resolvedParams["cameraType"] ?: "BACK"
        val saveDestination = resolvedParams["saveDestination"] ?: "GALLERY"
        
        callbacks.onLog("   📸 Iniciando captura de cámara en segundo plano: $cameraType")
        
        return CameraCaptureHelper.capturePhoto(
            context = context,
            cameraType = cameraType,
            saveDestination = saveDestination,
            callbacks = callbacks
        )
    }
}

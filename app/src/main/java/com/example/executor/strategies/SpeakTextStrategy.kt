package com.example.executor.strategies

import android.content.Context
import android.speech.tts.TextToSpeech
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.executor.ActionStrategy
import com.example.executor.ShortcutExecutionCallbacks

class SpeakTextStrategy : ActionStrategy {
    override suspend fun execute(
        context: Context,
        tts: TextToSpeech?,
        callbacks: ShortcutExecutionCallbacks,
        resolvedParams: Map<String, String>,
        currentInput: String
    ): String {
        val rawTextToSpeak = resolvedParams["text"] ?: ""
        val speedRateStr = resolvedParams["speechRate"] ?: "1.0"
        val speedRate = speedRateStr.toFloatOrNull() ?: 1.0f

        // Safeguard to prevent reading image files or binary content
        val isImagePath = rawTextToSpeak.trim().let { path ->
            (path.startsWith("/") || path.contains("/cache/") || path.contains("/files/")) && 
            (path.endsWith(".jpg", ignoreCase = true) || 
             path.endsWith(".jpeg", ignoreCase = true) || 
             path.endsWith(".png", ignoreCase = true) || 
             path.endsWith(".webp", ignoreCase = true))
        }

        val textToSpeak = if (isImagePath) {
            val fileName = rawTextToSpeak.substringAfterLast("/")
            "Foto capturada y guardada como $fileName"
        } else if (rawTextToSpeak.any { it.code < 9 && it.code != 10 && it.code != 13 }) {
            "No se puede reproducir el texto porque contiene datos binarios de un archivo."
        } else {
            rawTextToSpeak
        }
        
        val enginePackage = resolvedParams["engine"] ?: ""
        val voiceName = resolvedParams["voice"] ?: ""

        val activeTts = if (enginePackage.isNotEmpty()) {
            val ttsManager = com.example.executor.TtsManager.getInstance(context)
            val deferredEngine = kotlinx.coroutines.CompletableDeferred<android.speech.tts.TextToSpeech?>()
            withContext(Dispatchers.Main) {
                ttsManager.setEngine(enginePackage) { success ->
                    if (success) {
                        deferredEngine.complete(ttsManager.getTtsEngine())
                    } else {
                        deferredEngine.complete(tts)
                    }
                }
            }
            try {
                kotlinx.coroutines.withTimeout(3000) {
                    deferredEngine.await()
                }
            } catch (e: Exception) {
                tts
            }
        } else {
            tts
        }

        if (activeTts != null && voiceName.isNotEmpty()) {
            try {
                val voice = activeTts.voices?.find { it.name == voiceName }
                if (voice != null) {
                    activeTts.voice = voice
                }
            } catch (e: Exception) {
                // Ignore voice configuration errors
            }
        }
        
        callbacks.onLog("   🗣️ Hablando (velocidad: ${speedRate}x): \"$textToSpeak\"")
        if (activeTts != null && textToSpeak.isNotEmpty()) {
            val deferred = kotlinx.coroutines.CompletableDeferred<Unit>()
            val utteranceId = "shortcut_tts_${System.currentTimeMillis()}_${(1000..9999).random()}"
            
            val listener = object : android.speech.tts.UtteranceProgressListener() {
                override fun onStart(id: String?) {}
                override fun onDone(id: String?) {
                    if (id == utteranceId) {
                        deferred.complete(Unit)
                    }
                }
                @Deprecated("Deprecated in Java")
                override fun onError(id: String?) {
                    if (id == utteranceId) {
                        deferred.complete(Unit)
                    }
                }
                override fun onError(id: String?, errorCode: Int) {
                    if (id == utteranceId) {
                        deferred.complete(Unit)
                    }
                }
            }

            withContext(Dispatchers.Main) {
                activeTts.setOnUtteranceProgressListener(listener)
            }

            try {
                activeTts.setSpeechRate(speedRate)
            } catch (e: Exception) {
                // Fail-safe
            }

            val speakResult = activeTts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
            if (speakResult == TextToSpeech.ERROR) {
                callbacks.onLog("   ⚠️ Error al iniciar la reproducción TTS.")
                deferred.complete(Unit)
            } else {
                // Dynamic timeout based on characters (150ms per char) plus extra safety padding
                val timeoutMs = (textToSpeak.length * 150L).coerceAtLeast(4000L).coerceAtMost(45000L)
                try {
                    kotlinx.coroutines.withTimeout(timeoutMs) {
                        deferred.await()
                    }
                } catch (e: Exception) {
                    callbacks.onLog("   ⏳ Tiempo de espera TTS completado de manera anticipada o cancelado.")
                }
            }
        } else if (textToSpeak.isNotEmpty()) {
            withContext(Dispatchers.Main) {
                android.widget.Toast.makeText(context, textToSpeak, android.widget.Toast.LENGTH_LONG).show()
            }
        }
        return textToSpeak
    }
}

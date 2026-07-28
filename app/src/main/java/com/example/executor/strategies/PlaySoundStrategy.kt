package com.example.executor.strategies

import android.content.Context
import android.speech.tts.TextToSpeech
import android.media.AudioManager
import android.media.ToneGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.executor.ActionStrategy
import com.example.executor.ShortcutExecutionCallbacks

class PlaySoundStrategy : ActionStrategy {
    override suspend fun execute(
        context: Context,
        tts: TextToSpeech?,
        callbacks: ShortcutExecutionCallbacks,
        resolvedParams: Map<String, String>,
        currentInput: String
    ): String {
        val soundType = resolvedParams["soundType"] ?: "Beep"
        callbacks.onLog("   🔊 Reproduciendo sonido: $soundType")

        playSound(context, soundType) { logMessage ->
            callbacks.onLog("   ⚠️ $logMessage")
        }

        callbacks.onLog("   ✅ Sonido reproducido correctamente.")
        return currentInput
    }

    companion object {
        suspend fun playSound(context: Context, soundType: String, onErrorLog: ((String) -> Unit)? = null) {
            try {
                when (soundType) {
                    "Alert" -> {
                        val tg = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100)
                        tg.startTone(ToneGenerator.TONE_PROP_ACK, 200)
                    }
                    else -> {
                        val tg = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100)
                        tg.startTone(ToneGenerator.TONE_PROP_BEEP, 150)
                    }
                }
            } catch (e: Exception) {
                onErrorLog?.invoke("Error al reproducir sonido: ${e.localizedMessage}")
            }
        }
    }
}

package com.example.executor.strategies

import android.content.Context
import android.speech.tts.TextToSpeech
import com.example.executor.ActionStrategy
import com.example.executor.ShortcutExecutionCallbacks

class SetVolumeStrategy : ActionStrategy {
    override suspend fun execute(
        context: Context,
        tts: TextToSpeech?,
        callbacks: ShortcutExecutionCallbacks,
        resolvedParams: Map<String, String>,
        currentInput: String
    ): String {
        val streamTypeStr = resolvedParams["streamType"] ?: "Music"
        val volumePercentStr = resolvedParams["volumePercent"] ?: "50"
        val volumePercent = volumePercentStr.toIntOrNull()?.coerceIn(0, 100) ?: 50

        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? android.media.AudioManager
        if (audioManager != null) {
            val streamType = when (streamTypeStr) {
                "Music" -> android.media.AudioManager.STREAM_MUSIC
                "Ring" -> android.media.AudioManager.STREAM_RING
                "Notification" -> android.media.AudioManager.STREAM_NOTIFICATION
                "Alarm" -> android.media.AudioManager.STREAM_ALARM
                else -> android.media.AudioManager.STREAM_MUSIC
            }
            
            val maxVolume = audioManager.getStreamMaxVolume(streamType)
            val targetVolume = (maxVolume * (volumePercent / 100.0)).toInt()
            
            callbacks.onLog("   🔊 Ajustando volumen de \"$streamTypeStr\" al $volumePercent% ($targetVolume/$maxVolume)")
            try {
                audioManager.setStreamVolume(streamType, targetVolume, android.media.AudioManager.FLAG_SHOW_UI)
            } catch (e: Exception) {
                callbacks.onLog("   ⚠️ Falló al ajustar volumen: ${e.localizedMessage}")
            }
        } else {
            callbacks.onLog("   ⚠️ Servicio de Audio no disponible.")
        }
        return currentInput
    }
}

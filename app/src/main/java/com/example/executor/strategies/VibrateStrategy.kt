package com.example.executor.strategies

import android.content.Context
import android.speech.tts.TextToSpeech
import com.example.executor.ActionStrategy
import com.example.executor.ShortcutExecutionCallbacks

class VibrateStrategy : ActionStrategy {
    override suspend fun execute(
        context: Context,
        tts: TextToSpeech?,
        callbacks: ShortcutExecutionCallbacks,
        resolvedParams: Map<String, String>,
        currentInput: String
    ): String {
        val durationMs = resolvedParams["duration"]?.toLongOrNull() ?: 500L
        callbacks.onLog("   📳 Vibrando por ${durationMs}ms...")
        try {
            val vibrator = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as android.os.VibratorManager
                vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as android.os.Vibrator
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator.vibrate(android.os.VibrationEffect.createOneShot(durationMs, android.os.VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(durationMs)
            }
        } catch (e: Exception) {
            callbacks.onLog("⚠️ Error de vibración: ${e.localizedMessage}")
        }
        return durationMs.toString()
    }
}

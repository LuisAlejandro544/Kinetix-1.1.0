package com.example.executor.strategies

import android.content.Context
import android.speech.tts.TextToSpeech
import kotlinx.coroutines.delay
import com.example.executor.ActionStrategy
import com.example.executor.ShortcutExecutionCallbacks

class OpenAppStrategy : ActionStrategy {
    override suspend fun execute(
        context: Context,
        tts: TextToSpeech?,
        callbacks: ShortcutExecutionCallbacks,
        resolvedParams: Map<String, String>,
        currentInput: String
    ): String {
        val packageName = resolvedParams["packageName"] ?: ""
        val appName = resolvedParams["appName"] ?: packageName
        val delayStr = resolvedParams["delay"] ?: "3"
        val delaySeconds = delayStr.toIntOrNull() ?: 3

        if (delaySeconds > 0) {
            callbacks.onLog("   ⏳ Esperando $delaySeconds segundos antes de abrir \"$appName\"...")
            delay(delaySeconds * 1000L)
        }

        if (packageName.isNotEmpty()) {
            callbacks.onLog("   🚀 Iniciando aplicación: \"$appName\" ($packageName)")
            try {
                val intent = context.packageManager.getLaunchIntentForPackage(packageName)
                if (intent != null) {
                    intent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                } else {
                    callbacks.onLog("   ⚠️ No se encontró actividad de lanzamiento para: $packageName")
                }
            } catch (e: Exception) {
                callbacks.onLog("   ⚠️ Falló al abrir la aplicación: ${e.localizedMessage}")
            }
        } else {
            callbacks.onLog("   ⚠️ No se especificó el paquete de la aplicación.")
        }
        return currentInput
    }
}

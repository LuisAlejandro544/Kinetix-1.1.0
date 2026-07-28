package com.example.executor.strategies

import android.content.Context
import android.speech.tts.TextToSpeech
import kotlinx.coroutines.withContext
import com.example.executor.ActionStrategy
import com.example.executor.CpuDispatcherProvider
import com.example.executor.ShortcutExecutionCallbacks

class WriteFileStrategy : ActionStrategy {
    override suspend fun execute(
        context: Context,
        tts: TextToSpeech?,
        callbacks: ShortcutExecutionCallbacks,
        resolvedParams: Map<String, String>,
        currentInput: String
    ): String {
        val fileName = resolvedParams["fileName"] ?: "atajo_datos.txt"
        val content = resolvedParams["content"] ?: currentInput
        callbacks.onLog("   💾 Escribiendo archivo \"$fileName\" con contenido de tamaño ${content.length} (Cores LITTLE)...")
        return withContext(CpuDispatcherProvider.efficiencyDispatcher) {
            try {
                context.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
                    output.write(content.toByteArray())
                }
                callbacks.onLog("   ✅ Archivo guardado correctamente. ($fileName)")
                content
            } catch (e: Exception) {
                callbacks.onLog("   ⚠️ Error al escribir archivo: ${e.localizedMessage}")
                currentInput
            }
        }
    }
}

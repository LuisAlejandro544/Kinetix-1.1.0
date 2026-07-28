package com.example.executor.strategies

import android.content.Context
import android.speech.tts.TextToSpeech
import kotlinx.coroutines.withContext
import com.example.executor.ActionStrategy
import com.example.executor.CpuDispatcherProvider
import com.example.executor.ShortcutExecutionCallbacks

class ReadFileStrategy : ActionStrategy {
    override suspend fun execute(
        context: Context,
        tts: TextToSpeech?,
        callbacks: ShortcutExecutionCallbacks,
        resolvedParams: Map<String, String>,
        currentInput: String
    ): String {
        val fileName = resolvedParams["fileName"] ?: "atajo_datos.txt"
        callbacks.onLog("   📖 Leyendo archivo \"$fileName\" (Cores LITTLE)...")
        return withContext(CpuDispatcherProvider.efficiencyDispatcher) {
            try {
                val file = java.io.File(context.filesDir, fileName)
                if (file.exists()) {
                    val nameLower = file.name.lowercase()
                    if (nameLower.endsWith(".jpg") || nameLower.endsWith(".jpeg") || 
                        nameLower.endsWith(".png") || nameLower.endsWith(".gif") || 
                        nameLower.endsWith(".webp") || nameLower.endsWith(".bin") ||
                        nameLower.endsWith(".apk") || nameLower.endsWith(".jks") ||
                        nameLower.endsWith(".zip") || nameLower.endsWith(".rar") ||
                        nameLower.endsWith(".pdf") || nameLower.endsWith(".mp3") ||
                        nameLower.endsWith(".mp4")) {
                        callbacks.onLog("   ⚠️ Advertencia: No se puede leer el archivo binario/imagen \"$fileName\" como texto.")
                        return@withContext "Archivo binario: ${file.name}"
                    }
                    val text = file.readText()
                    callbacks.onLog("   ✅ Archivo leído. Contenido: \"$text\"")
                    text
                } else {
                    callbacks.onLog("   ⚠️ Archivo no encontrado: \"$fileName\"")
                    ""
                }
            } catch (e: Exception) {
                callbacks.onLog("   ⚠️ Error al leer archivo: ${e.localizedMessage}")
                ""
            }
        }
    }
}

package com.example.executor.strategies

import android.content.Context
import android.speech.tts.TextToSpeech
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.executor.ActionStrategy
import com.example.executor.ShortcutExecutionCallbacks

class AppendFileStrategy : ActionStrategy {
    override suspend fun execute(
        context: Context,
        tts: TextToSpeech?,
        callbacks: ShortcutExecutionCallbacks,
        resolvedParams: Map<String, String>,
        currentInput: String
    ): String {
        val fileName = resolvedParams["fileName"] ?: "atajo_log.txt"
        val content = resolvedParams["content"] ?: currentInput
        callbacks.onLog("   📝 Añadiendo a archivo \"$fileName\": \"$content\"...")
        return withContext(Dispatchers.IO) {
            try {
                context.openFileOutput(fileName, Context.MODE_APPEND).use { output ->
                    output.write((content + "\n").toByteArray())
                }
                callbacks.onLog("   ✅ Texto añadido correctamente a $fileName.")
                content
            } catch (e: Exception) {
                callbacks.onLog("   ⚠️ Error al añadir al archivo: ${e.localizedMessage}")
                currentInput
            }
        }
    }
}

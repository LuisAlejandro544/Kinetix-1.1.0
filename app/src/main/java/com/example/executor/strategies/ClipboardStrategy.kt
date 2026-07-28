package com.example.executor.strategies

import android.content.Context
import android.speech.tts.TextToSpeech
import android.content.ClipboardManager
import android.content.ClipData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.executor.ActionStrategy
import com.example.executor.ShortcutExecutionCallbacks

class ClipboardStrategy : ActionStrategy {
    override suspend fun execute(
        context: Context,
        tts: TextToSpeech?,
        callbacks: ShortcutExecutionCallbacks,
        resolvedParams: Map<String, String>,
        currentInput: String
    ): String = withContext(Dispatchers.Main) {
        val operation = resolvedParams["operation"] ?: "WRITE"
        val text = resolvedParams["text"] ?: currentInput
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        if (operation == "WRITE") {
            callbacks.onLog("   📋 Copiando al portapapeles: \"$text\"")
            val clip = ClipData.newPlainText("Kinetix", text)
            clipboard.setPrimaryClip(clip)
            callbacks.onLog("   ✅ Copiado correctamente.")
            text
        } else {
            callbacks.onLog("   📋 Leyendo del portapapeles...")
            val clipData = clipboard.primaryClip
            if (clipData != null && clipData.itemCount > 0) {
                val item = clipData.getItemAt(0)
                val clipboardText = item.text?.toString() ?: ""
                callbacks.onLog("   ✅ Leído correctamente: \"$clipboardText\"")
                clipboardText
            } else {
                callbacks.onLog("   ⚠️ Portapapeles vacío o no contiene texto.")
                ""
            }
        }
    }
}

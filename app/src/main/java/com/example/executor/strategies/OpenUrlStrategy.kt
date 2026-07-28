package com.example.executor.strategies

import android.content.Context
import android.speech.tts.TextToSpeech
import com.example.executor.ActionStrategy
import com.example.executor.ShortcutExecutionCallbacks

class OpenUrlStrategy : ActionStrategy {
    override suspend fun execute(
        context: Context,
        tts: TextToSpeech?,
        callbacks: ShortcutExecutionCallbacks,
        resolvedParams: Map<String, String>,
        currentInput: String
    ): String {
        val url = resolvedParams["url"] ?: ""
        callbacks.onLog("   🌐 Abriendo URL: $url")
        try {
            val webUri = if (!url.startsWith("http://") && !url.startsWith("https://")) {
                "https://$url"
            } else {
                url
            }
            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(webUri)).apply {
                flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            callbacks.onLog("⚠️ No se pudo abrir la URL: ${e.localizedMessage}")
        }
        return url
    }
}

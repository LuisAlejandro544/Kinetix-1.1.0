package com.example.executor.strategies

import android.content.Context
import android.speech.tts.TextToSpeech
import kotlinx.coroutines.withContext
import com.example.executor.ActionStrategy
import com.example.executor.CpuDispatcherProvider
import com.example.executor.ShortcutExecutionCallbacks

class CustomCodeStrategy : ActionStrategy {
    override suspend fun execute(
        context: Context,
        tts: TextToSpeech?,
        callbacks: ShortcutExecutionCallbacks,
        resolvedParams: Map<String, String>,
        currentInput: String
    ): String = withContext(CpuDispatcherProvider.performanceDispatcher) {
        val script = resolvedParams["script"] ?: ""
        callbacks.onLog("   ⚙️ Ejecutando Bloque de Código Personalizado (Cores big)...")
        
        CustomCodeInterpreter.execute(
            script = script,
            currentInput = currentInput,
            tts = tts,
            callbacks = callbacks
        )
    }
}

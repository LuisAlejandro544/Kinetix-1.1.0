package com.example.executor.strategies

import android.content.Context
import android.speech.tts.TextToSpeech
import app.cash.quickjs.QuickJs
import kotlinx.coroutines.withContext
import com.example.executor.ActionStrategy
import com.example.executor.CpuDispatcherProvider
import com.example.executor.ShortcutExecutionCallbacks

class ExecJavaScriptStrategy : ActionStrategy {
    override suspend fun execute(
        context: Context,
        tts: TextToSpeech?,
        callbacks: ShortcutExecutionCallbacks,
        resolvedParams: Map<String, String>,
        currentInput: String
    ): String = withContext(CpuDispatcherProvider.performanceDispatcher) {
        val code = resolvedParams["code"] ?: ""
        callbacks.onLog("   💻 Ejecutando código JS en QuickJS (Cores big)...")
        
        try {
            val quickJs = QuickJs.create()
            val result = try {
                val evaluated = quickJs.evaluate(code)
                evaluated?.toString() ?: ""
            } finally {
                quickJs.close()
            }
            callbacks.onLog("   ✅ Ejecución QuickJS completada. Salida: \"$result\"")
            result
        } catch (e: Exception) {
            callbacks.onLog("   ❌ Error en JavaScript: ${e.localizedMessage}")
            "Error: ${e.localizedMessage}"
        }
    }
}

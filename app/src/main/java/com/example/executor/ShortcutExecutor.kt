package com.example.executor

import android.content.Context
import android.speech.tts.TextToSpeech
import com.example.data.ActionData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ShortcutExecutor(
    private val context: Context,
    private val tts: TextToSpeech?,
    private val callbacks: ShortcutExecutionCallbacks
) {
    // Utilize the modular ActionStrategyRegistry
    private val strategies = ActionStrategyRegistry.strategies

    private val attributedContext: Context = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
        context.createAttributionContext("default")
    } else {
        context
    }

    suspend fun execute(actions: List<ActionData>): String = withContext(Dispatchers.Default) {
        var currentInput = ""
        val totalSteps = actions.size

        if (totalSteps == 0) {
            callbacks.onLog("⚠️ No hay acciones en este atajo para ejecutar.")
            return@withContext ""
        }

        for (index in actions.indices) {
            val action = actions[index]
            callbacks.onProgress(index + 1, totalSteps, action.type.displayName)
            callbacks.onLog("▶️ Paso ${index + 1} de $totalSteps: ${action.type.displayName}")

            // Resolve placeholders in parameters using VariableResolver
            val resolvedParams = VariableResolver.resolvePlaceholders(action.params, currentInput)

            try {
                val strategy = strategies[action.type]
                if (strategy != null) {
                    currentInput = strategy.execute(
                        context = attributedContext,
                        tts = tts,
                        callbacks = callbacks,
                        resolvedParams = resolvedParams,
                        currentInput = currentInput
                    )
                } else {
                    callbacks.onLog("⚠️ Error: Acción ${action.type.name} no soportada.")
                }
            } catch (e: Exception) {
                callbacks.onLog("❌ Error en el Paso ${index + 1}: ${e.localizedMessage}")
                throw e
            }
        }
        callbacks.onLog("🎉 ¡Atajo completado con éxito!")
        currentInput
    }
}

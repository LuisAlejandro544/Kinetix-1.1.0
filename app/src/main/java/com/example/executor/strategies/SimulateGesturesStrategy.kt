package com.example.executor.strategies

import android.content.Context
import android.speech.tts.TextToSpeech
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.executor.ActionStrategy
import com.example.executor.ShortcutExecutionCallbacks
import com.example.executor.ShortcutAccessibilityService

class SimulateGesturesStrategy : ActionStrategy {
    override suspend fun execute(
        context: Context,
        tts: TextToSpeech?,
        callbacks: ShortcutExecutionCallbacks,
        resolvedParams: Map<String, String>,
        currentInput: String
    ): String = withContext(com.example.executor.CpuDispatcherProvider.performanceDispatcher) {
        val gestureType = resolvedParams["gestureType"] ?: "TAP"
        val x1 = resolvedParams["x1"]?.toFloatOrNull() ?: 500f
        val y1 = resolvedParams["y1"]?.toFloatOrNull() ?: 1000f
        val x2 = resolvedParams["x2"]?.toFloatOrNull() ?: 500f
        val y2 = resolvedParams["y2"]?.toFloatOrNull() ?: 500f
        val duration = resolvedParams["duration"]?.toLongOrNull() ?: 300L

        callbacks.onLog("   🎮 Simulando Gesto: $gestureType (Inicio: $x1, $y1) (Cores big)")

        if (!ShortcutAccessibilityService.isServiceRunning()) {
            callbacks.onLog("   ⚠️ El servicio de accesibilidad de 'Kinetix' no está activo.")
            callbacks.onLog("   🚀 Redirigiendo a los ajustes de accesibilidad para activarlo...")
            withContext(Dispatchers.Main) {
                try {
                    val intent = android.content.Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                        flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                } catch (e: Exception) {
                    callbacks.onLog("   ⚠️ No se pudieron abrir los ajustes de accesibilidad: ${e.localizedMessage}")
                }
            }
            return@withContext "Servicio no activo"
        }

        val path = android.graphics.Path()
        path.moveTo(x1, y1)
        if (gestureType == "SWIPE") {
            path.lineTo(x2, y2)
        }

        val success = ShortcutAccessibilityService.simulateGesture(path, duration)
        if (success) {
            callbacks.onLog("   ✅ Gesto simulado correctamente.")
        } else {
            callbacks.onLog("   ❌ No se pudo simular el gesto.")
        }

        return@withContext currentInput
    }
}

package com.example.executor.strategies

import android.content.Context
import android.speech.tts.TextToSpeech
import com.example.executor.ActionStrategy
import com.example.executor.ShortcutExecutionCallbacks
import com.example.executor.ShortcutAccessibilityService

class AccessibilityActionStrategy : ActionStrategy {
    override suspend fun execute(
        context: Context,
        tts: TextToSpeech?,
        callbacks: ShortcutExecutionCallbacks,
        resolvedParams: Map<String, String>,
        currentInput: String
    ): String {
        val actionType = resolvedParams["actionType"] ?: "Back"
        callbacks.onLog("   ♿ Ejecutando Acción de Accesibilidad: $actionType")

        if (!ShortcutAccessibilityService.isServiceRunning()) {
            callbacks.onLog("   ⚠️ El servicio de accesibilidad de 'Kinetix' no está activo.")
            callbacks.onLog("   🚀 Redirigiendo a los ajustes de accesibilidad de Android para que puedas activarlo...")
            try {
                val intent = android.content.Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                    flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
            } catch (e: Exception) {
                callbacks.onLog("   ⚠️ No se pudieron abrir los ajustes de accesibilidad: ${e.localizedMessage}")
            }
            return "Servicio no activo"
        }

        val globalActionId = when (actionType) {
            "Back" -> android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK
            "Home" -> android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_HOME
            "Notifications" -> android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_NOTIFICATIONS
            "Quick Settings" -> android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_QUICK_SETTINGS
            "Power Dialog" -> android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_POWER_DIALOG
            "Lock Screen" -> {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                    android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_LOCK_SCREEN
                } else {
                    -1
                }
            }
            else -> android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK
        }

        if (globalActionId == -1) {
            callbacks.onLog("   ⚠️ Acción '$actionType' requiere una versión de Android superior.")
            return currentInput
        }

        val success = ShortcutAccessibilityService.performGlobalAction(globalActionId)
        if (success) {
            callbacks.onLog("   ✅ Acción de accesibilidad '$actionType' completada con éxito.")
        } else {
            callbacks.onLog("   ❌ Falló al ejecutar la acción de accesibilidad '$actionType'.")
        }
        return currentInput
    }
}

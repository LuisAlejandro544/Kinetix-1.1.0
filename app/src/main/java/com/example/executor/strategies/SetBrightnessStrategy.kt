package com.example.executor.strategies

import android.content.Context
import android.speech.tts.TextToSpeech
import com.example.executor.ActionStrategy
import com.example.executor.ShortcutExecutionCallbacks

class SetBrightnessStrategy : ActionStrategy {
    override suspend fun execute(
        context: Context,
        tts: TextToSpeech?,
        callbacks: ShortcutExecutionCallbacks,
        resolvedParams: Map<String, String>,
        currentInput: String
    ): String {
        val brightnessPercentStr = resolvedParams["brightnessPercent"] ?: "70"
        val percent = brightnessPercentStr.toIntOrNull()?.coerceIn(0, 100) ?: 70
        val brightnessValue = (percent * 255 / 100).coerceIn(0, 255)

        callbacks.onLog("   🔆 Ajustando brillo de pantalla al $percent% ($brightnessValue/255)")

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (android.provider.Settings.System.canWrite(context)) {
                try {
                    android.provider.Settings.System.putInt(
                        context.contentResolver,
                        android.provider.Settings.System.SCREEN_BRIGHTNESS,
                        brightnessValue
                    )
                    callbacks.onLog("   ✅ Brillo de pantalla ajustado con éxito.")
                } catch (e: Exception) {
                    callbacks.onLog("   ⚠️ No se pudo ajustar el brillo: ${e.localizedMessage}")
                }
            } else {
                callbacks.onLog("   ⚠️ Falta el permiso de \"Modificar ajustes del sistema\" para cambiar el brillo.")
                callbacks.onLog("   🚀 Redirigiendo a la pantalla de permisos del sistema para que puedas otorgarlo...")
                try {
                    val intent = android.content.Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                        data = android.net.Uri.parse("package:" + context.packageName)
                        flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                } catch (e: Exception) {
                    callbacks.onLog("   ⚠️ No se pudo abrir la pantalla de permisos: ${e.localizedMessage}")
                }
            }
        } else {
            try {
                android.provider.Settings.System.putInt(
                    context.contentResolver,
                    android.provider.Settings.System.SCREEN_BRIGHTNESS,
                    brightnessValue
                )
                callbacks.onLog("   ✅ Brillo de pantalla ajustado con éxito.")
            } catch (e: Exception) {
                callbacks.onLog("   ⚠️ No se pudo ajustar el brillo: ${e.localizedMessage}")
            }
        }
        return percent.toString()
    }
}

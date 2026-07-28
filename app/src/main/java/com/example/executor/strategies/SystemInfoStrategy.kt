package com.example.executor.strategies

import android.content.Context
import android.speech.tts.TextToSpeech
import com.example.executor.ActionStrategy
import com.example.executor.ShortcutExecutionCallbacks

class SystemInfoStrategy : ActionStrategy {
    override suspend fun execute(
        context: Context,
        tts: TextToSpeech?,
        callbacks: ShortcutExecutionCallbacks,
        resolvedParams: Map<String, String>,
        currentInput: String
    ): String {
        val infoType = resolvedParams["infoType"] ?: "Battery Level"
        callbacks.onLog("   📱 Consultando información: $infoType")
        val result = when (infoType) {
            "Battery Level" -> {
                try {
                    val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as android.os.BatteryManager
                    val level = batteryManager.getIntProperty(android.os.BatteryManager.BATTERY_PROPERTY_CAPACITY)
                    "$level%"
                } catch (e: Exception) {
                    "50% (Simulado)"
                }
            }
            "Device Model" -> "${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}"
            "Android Version" -> "Android ${android.os.Build.VERSION.RELEASE} (API ${android.os.Build.VERSION.SDK_INT})"
            else -> "Desconocido"
        }
        callbacks.onLog("   📋 Información obtenida: \"$result\"")
        return result
    }
}

package com.example.executor

import android.content.Context
import com.example.data.ShortcutDatabase

object ChargerTriggerHandler {
    suspend fun processChargerEvent(context: Context, isConnected: Boolean) {
        try {
            val database = ShortcutDatabase.getDatabase(context.applicationContext)
            val shortcuts = database.shortcutDao().getAllShortcutsList()
            
            shortcuts.forEach { shortcut ->
                if (shortcut.isChargerTriggerEnabled) {
                    val expectedType = shortcut.triggerChargerType ?: "CONNECTED"
                    val isDisconnected = !isConnected
                    val shouldTrigger = (expectedType == "CONNECTED" && isConnected) || 
                                        (expectedType == "DISCONNECTED" && isDisconnected)
                    if (shouldTrigger) {
                        BackgroundExecutor.executeShortcutInBackground(context, shortcut)
                    }
                }
            }
        } catch (e: Exception) {
            FileLogManager.logWarning(
                context,
                "BackgroundTriggerReceiverError",
                "Error procesando atajos de cargador: ${e.localizedMessage}"
            )
        }
    }
}

package com.example.executor

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BackgroundTriggerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        val isConnected = action == Intent.ACTION_POWER_CONNECTED
        val isDisconnected = action == Intent.ACTION_POWER_DISCONNECTED
        
        if (!isConnected && !isDisconnected) return

        val stateStr = if (isConnected) "conectado" else "desconectado"
        FileLogManager.logWarning(context, "BackgroundTriggerReceiver", "Cargador $stateStr detectado en receptor estático.")

        // Ensure foreground service is running
        KinetixForegroundService.startService(context)

        // Delegate trigger handling to ChargerTriggerHandler on IO dispatcher
        CoroutineScope(Dispatchers.IO).launch {
            ChargerTriggerHandler.processChargerEvent(context, isConnected)
        }
    }
}

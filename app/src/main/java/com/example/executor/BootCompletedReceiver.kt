package com.example.executor

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        if (action == Intent.ACTION_BOOT_COMPLETED ||
            action == Intent.ACTION_LOCKED_BOOT_COMPLETED ||
            action == Intent.ACTION_MY_PACKAGE_REPLACED
        ) {
            FileLogManager.logWarning(
                context,
                "BootCompletedReceiver",
                "Inicio de sistema o actualización detectada ($action). Iniciando KinetixForegroundService..."
            )
            KinetixForegroundService.startService(context)
        }
    }
}

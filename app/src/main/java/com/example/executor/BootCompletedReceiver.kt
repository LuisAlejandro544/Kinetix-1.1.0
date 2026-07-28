package com.example.executor

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
            
            // Sync all scheduled alarms after boot
            val appContext = context.applicationContext
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val db = com.example.data.ShortcutDatabase.getDatabase(appContext)
                    val shortcuts = db.shortcutDao().getAllShortcutsList()
                    ScheduleTriggerManager.syncAllScheduleAlarms(appContext, shortcuts)
                } catch (e: Exception) {
                    FileLogManager.logWarning(appContext, "BootCompletedReceiver", "Error al resincronizar alarmas: ${e.localizedMessage}")
                }
            }
        }
    }
}

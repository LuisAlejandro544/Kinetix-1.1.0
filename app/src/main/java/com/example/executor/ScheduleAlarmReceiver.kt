package com.example.executor

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.data.ShortcutDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScheduleAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val shortcutId = intent.getIntExtra("EXTRA_SHORTCUT_ID", -1)
        if (shortcutId == -1) return

        val appContext = context.applicationContext
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = ShortcutDatabase.getDatabase(appContext)
                val shortcut = db.shortcutDao().getShortcutByIdSync(shortcutId)
                if (shortcut != null && shortcut.isScheduleTriggerEnabled) {
                    FileLogManager.logWarning(
                        appContext,
                        "ScheduleAlarmReceiver",
                        "⏰ Disparador programado activado para atajo: ${shortcut.name}"
                    )
                    BackgroundExecutor.executeShortcutInBackground(appContext, shortcut)
                    // Re-schedule for next period/day
                    ScheduleTriggerManager.scheduleShortcutAlarm(appContext, shortcut)
                }
            } catch (e: Exception) {
                FileLogManager.logWarning(
                    appContext,
                    "ScheduleAlarmReceiverError",
                    "Error al procesar alarma programada: ${e.localizedMessage}"
                )
            }
        }
    }
}

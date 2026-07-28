package com.example.executor

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.data.Shortcut
import java.util.*

object ScheduleTriggerManager {
    private const val ACTION_SCHEDULE_TRIGGER = "com.example.kinetix.ACTION_SCHEDULE_TRIGGER"

    fun scheduleShortcutAlarm(context: Context, shortcut: Shortcut) {
        if (!shortcut.isScheduleTriggerEnabled || shortcut.triggerScheduleTime.isNullOrBlank()) {
            cancelShortcutAlarm(context, shortcut.id)
            return
        }

        try {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
            val parts = shortcut.triggerScheduleTime.split(":")
            if (parts.size < 2) return

            val hour = parts[0].trim().toIntOrNull() ?: 8
            val minute = parts[1].trim().toIntOrNull() ?: 30

            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            // If time is earlier today, move to tomorrow
            val now = System.currentTimeMillis()
            if (calendar.timeInMillis <= now) {
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            }

            // Check day filter if specified
            val daysStr = shortcut.triggerScheduleDays ?: "DAILY"
            if (daysStr != "DAILY" && daysStr.isNotBlank()) {
                val allowedDays = daysStr.split(",").map { it.trim().uppercase() }
                val dayMap = mapOf(
                    Calendar.MONDAY to "MON",
                    Calendar.TUESDAY to "TUE",
                    Calendar.WEDNESDAY to "WED",
                    Calendar.THURSDAY to "THU",
                    Calendar.FRIDAY to "FRI",
                    Calendar.SATURDAY to "SAT",
                    Calendar.SUNDAY to "SUN"
                )

                // Advance calendar day until it matches an allowed day
                var attempts = 0
                while (attempts < 7) {
                    val dayCode = dayMap[calendar.get(Calendar.DAY_OF_WEEK)]
                    if (dayCode != null && allowedDays.contains(dayCode)) {
                        break
                    }
                    calendar.add(Calendar.DAY_OF_YEAR, 1)
                    attempts++
                }
            }

            val triggerAtMs = calendar.timeInMillis
            val intent = Intent(context, ScheduleAlarmReceiver::class.java).apply {
                action = ACTION_SCHEDULE_TRIGGER
                putExtra("EXTRA_SHORTCUT_ID", shortcut.id)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                shortcut.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMs,
                    pendingIntent
                )
            } else {
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMs,
                    pendingIntent
                )
            }

            FileLogManager.logWarning(
                context,
                "ScheduleTriggerManager",
                "⏰ Alarma programada para atajo \"${shortcut.name}\" a las ${shortcut.triggerScheduleTime} (Próxima: ${calendar.time})"
            )
        } catch (e: Exception) {
            FileLogManager.logWarning(
                context,
                "ScheduleTriggerManagerError",
                "Error al programar alarma: ${e.localizedMessage}"
            )
        }
    }

    fun cancelShortcutAlarm(context: Context, shortcutId: Int) {
        try {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
            val intent = Intent(context, ScheduleAlarmReceiver::class.java).apply {
                action = ACTION_SCHEDULE_TRIGGER
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                shortcutId,
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
            }
        } catch (e: Exception) {
            // Ignore
        }
    }

    fun syncAllScheduleAlarms(context: Context, shortcuts: List<Shortcut>) {
        shortcuts.forEach { shortcut ->
            if (shortcut.isScheduleTriggerEnabled) {
                scheduleShortcutAlarm(context, shortcut)
            } else {
                cancelShortcutAlarm(context, shortcut.id)
            }
        }
    }
}

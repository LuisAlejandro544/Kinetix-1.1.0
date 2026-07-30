package com.example.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.MainActivity
import com.example.R
import com.example.data.ShortcutDatabase
import com.example.executor.BackgroundExecutor
import com.example.executor.FileLogManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KinetixWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            try {
                val db = ShortcutDatabase.getDatabase(context)
                val shortcuts = db.shortcutDao().getAllShortcutsList()
                val topShortcut = shortcuts.firstOrNull()

                for (appWidgetId in appWidgetIds) {
                    val views = RemoteViews(context.packageName, R.layout.widget_kinetix)

                    // Intent to open main app
                    val openAppIntent = Intent(context, MainActivity::class.java)
                    val openAppPendingIntent = PendingIntent.getActivity(
                        context,
                        0,
                        openAppIntent,
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )
                    views.setOnClickPendingIntent(R.id.widget_open_btn, openAppPendingIntent)

                    if (topShortcut != null) {
                        views.setTextViewText(R.id.widget_status_text, "⚡ Ejecutar: ${topShortcut.name}")

                        val runIntent = Intent(context, KinetixWidgetProvider::class.java).apply {
                            action = ACTION_EXECUTE_WIDGET_SHORTCUT
                            putExtra(EXTRA_SHORTCUT_ID, topShortcut.id)
                        }
                        val runPendingIntent = PendingIntent.getBroadcast(
                            context,
                            topShortcut.id,
                            runIntent,
                            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                        )
                        views.setOnClickPendingIntent(R.id.widget_shortcuts_container, runPendingIntent)
                    } else {
                        views.setTextViewText(R.id.widget_status_text, "Abre Kinetix para crear tu primer atajo")
                        views.setOnClickPendingIntent(R.id.widget_shortcuts_container, openAppPendingIntent)
                    }

                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            } catch (e: Exception) {
                FileLogManager.logWarning(context, "WidgetError", "Error al actualizar widget: ${e.localizedMessage}")
            }
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_EXECUTE_WIDGET_SHORTCUT) {
            val shortcutId = intent.getIntExtra(EXTRA_SHORTCUT_ID, -1)
            if (shortcutId != -1) {
                val scope = CoroutineScope(Dispatchers.Default)
                scope.launch {
                    try {
                        val db = ShortcutDatabase.getDatabase(context)
                        val shortcut = db.shortcutDao().getShortcutByIdSync(shortcutId)
                        if (shortcut != null) {
                            BackgroundExecutor.executeShortcutInBackground(context, shortcut)
                        }
                    } catch (e: Exception) {
                        FileLogManager.logWarning(context, "WidgetExecutionError", "Error ejecutando atajo desde widget: ${e.localizedMessage}")
                    }
                }
            }
        }
    }

    companion object {
        const val ACTION_EXECUTE_WIDGET_SHORTCUT = "com.example.kinetix.ACTION_EXECUTE_WIDGET_SHORTCUT"
        const val EXTRA_SHORTCUT_ID = "extra_shortcut_id"
    }
}

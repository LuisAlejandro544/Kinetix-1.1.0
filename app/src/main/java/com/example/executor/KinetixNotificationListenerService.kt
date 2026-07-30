package com.example.executor

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.example.data.ShortcutDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class KinetixNotificationListenerService : NotificationListenerService() {

    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        if (sbn == null) return

        val packageName = sbn.packageName ?: ""
        // Do not process notifications originating from Kinetix itself to prevent loops
        if (packageName == applicationContext.packageName) return

        val extras = sbn.notification?.extras ?: return
        val title = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString() ?: ""
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString() ?: ""

        serviceScope.launch {
            try {
                val db = ShortcutDatabase.getDatabase(applicationContext)
                val shortcuts = db.shortcutDao().getAllShortcutsList()

                shortcuts.forEach { shortcut ->
                    if (shortcut.isNotificationTriggerEnabled) {
                        val filterApp = shortcut.triggerNotificationApp?.trim() ?: ""
                        val filterKeyword = shortcut.triggerNotificationKeyword?.trim() ?: ""

                        val appMatches = filterApp.isEmpty() || packageName.contains(filterApp, ignoreCase = true)
                        val keywordMatches = filterKeyword.isEmpty() ||
                                title.contains(filterKeyword, ignoreCase = true) ||
                                text.contains(filterKeyword, ignoreCase = true)

                        if (appMatches && keywordMatches) {
                            FileLogManager.logWarning(
                                applicationContext,
                                "NotificationTrigger",
                                "Notificación recibida de $packageName (\"$title\"). Disparando atajo: ${shortcut.name}"
                            )
                            BackgroundExecutor.executeShortcutInBackground(applicationContext, shortcut)
                        }
                    }
                }
            } catch (e: Exception) {
                FileLogManager.logWarning(
                    applicationContext,
                    "NotificationTriggerError",
                    "Error procesando notificación: ${e.localizedMessage}"
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}

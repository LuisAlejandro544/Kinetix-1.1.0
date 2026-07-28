package com.example.executor

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import com.example.data.ShortcutDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class KinetixForegroundService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var systemTriggerManager: SystemTriggerManager? = null

    private var lastSeenBatteryLevel: Int? = null
    private val triggeredShortcutsForLevel = mutableSetOf<Int>()
    private var lastSeenChargerState: Boolean? = null
    private var lastSeenHeadphonesState: Boolean? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        FileLogManager.logWarning(this, "KinetixForegroundService", "Servicio en primer plano creado.")

        try {
            systemTriggerManager = SystemTriggerManager(
                context = applicationContext,
                onBatteryLevelChanged = { percentage -> handleBatteryChange(percentage) },
                onChargerChanged = { connected -> handleChargerChange(connected) },
                onHeadphonesChanged = { connected -> handleHeadphonesChange(connected) }
            ).apply {
                register()
            }
        } catch (e: Exception) {
            FileLogManager.logWarning(this, "KinetixForegroundService", "Error registrando monitores: ${e.localizedMessage}")
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Kinetix Atajos Activo")
            .setContentText("Monitoreando disparadores y automatizaciones en segundo plano")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                if (Build.VERSION.SDK_INT >= 34) {
                    startForeground(
                        NOTIFICATION_ID,
                        notification,
                        android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
                    )
                } else {
                    startForeground(NOTIFICATION_ID, notification)
                }
            } catch (e: Exception) {
                // Fallback standard foreground call if type registration differs on vendor
                startForeground(NOTIFICATION_ID, notification)
            }
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        systemTriggerManager?.unregister()
        systemTriggerManager = null
        serviceScope.cancel()
        FileLogManager.logWarning(this, "KinetixForegroundService", "Servicio en primer plano destruido.")
    }

    private fun handleBatteryChange(percentage: Int) {
        val previousLevel = lastSeenBatteryLevel
        if (previousLevel == percentage) return
        lastSeenBatteryLevel = percentage
        triggeredShortcutsForLevel.clear()

        serviceScope.launch {
            try {
                val db = ShortcutDatabase.getDatabase(applicationContext)
                val shortcuts = db.shortcutDao().getAllShortcutsList()
                shortcuts.forEach { shortcut ->
                    if (shortcut.isBatteryTriggerEnabled && shortcut.triggerBatteryLevel == percentage) {
                        val type = shortcut.triggerBatteryType ?: "EQUALS"
                        var shouldTrigger = false

                        when (type) {
                            "EQUALS" -> shouldTrigger = true
                            "FALLS_BELOW" -> {
                                if (previousLevel == null || previousLevel > percentage) {
                                    shouldTrigger = true
                                }
                            }
                            "RISES_ABOVE" -> {
                                if (previousLevel == null || previousLevel < percentage) {
                                    shouldTrigger = true
                                }
                            }
                        }

                        if (shouldTrigger && !triggeredShortcutsForLevel.contains(shortcut.id)) {
                            triggeredShortcutsForLevel.add(shortcut.id)
                            BackgroundExecutor.executeShortcutInBackground(applicationContext, shortcut)
                        }
                    }
                }
            } catch (e: Exception) {
                // Catch safely
            }
        }
    }

    private fun handleChargerChange(connected: Boolean) {
        if (lastSeenChargerState == connected) return
        lastSeenChargerState = connected

        serviceScope.launch {
            ChargerTriggerHandler.processChargerEvent(applicationContext, connected)
        }
    }

    private fun handleHeadphonesChange(connected: Boolean) {
        if (lastSeenHeadphonesState == connected) return
        lastSeenHeadphonesState = connected

        serviceScope.launch {
            try {
                val db = ShortcutDatabase.getDatabase(applicationContext)
                val shortcuts = db.shortcutDao().getAllShortcutsList()
                shortcuts.forEach { shortcut ->
                    if (shortcut.isHeadphonesTriggerEnabled) {
                        val expectedType = shortcut.triggerHeadphonesType ?: "CONNECTED"
                        val shouldTrigger = (expectedType == "CONNECTED" && connected) || 
                                            (expectedType == "DISCONNECTED" && !connected)
                        if (shouldTrigger) {
                            BackgroundExecutor.executeShortcutInBackground(applicationContext, shortcut)
                        }
                    }
                }
            } catch (e: Exception) {
                // Catch safely
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Kinetix Servicio en Segundo Plano"
            val descriptionText = "Notificación persistente para ejecutar atajos y monitorear disparadores del sistema"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "kinetix_foreground_service_channel"
        const val NOTIFICATION_ID = 1001

        fun startService(context: Context) {
            try {
                val intent = Intent(context, KinetixForegroundService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent)
                } else {
                    context.startService(intent)
                }
            } catch (e: Exception) {
                FileLogManager.logWarning(context, "KinetixForegroundService", "Error iniciando servicio: ${e.localizedMessage}")
            }
        }

        fun stopService(context: Context) {
            try {
                val intent = Intent(context, KinetixForegroundService::class.java)
                context.stopService(intent)
            } catch (e: Exception) {
                // Ignore
            }
        }
    }
}

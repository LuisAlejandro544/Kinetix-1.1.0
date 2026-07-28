package com.example.executor

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.view.accessibility.AccessibilityEvent
import kotlinx.coroutines.launch

class ShortcutAccessibilityService : AccessibilityService() {

    private var systemTriggerManager: SystemTriggerManager? = null
    private val serviceScope = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Default + kotlinx.coroutines.SupervisorJob())
    private var lastSeenBatteryLevel: Int? = null
    private val triggeredShortcutsForLevel = mutableSetOf<Int>()
    private var lastSeenChargerState: Boolean? = null
    private var lastSeenHeadphonesState: Boolean? = null

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Not needed for simple global action execution
    }

    override fun onInterrupt() {
        // Not needed
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this

        try {
            val systemTrigger = SystemTriggerManager(
                context = applicationContext,
                onBatteryLevelChanged = { percentage -> handleBatteryChange(percentage) },
                onChargerChanged = { connected -> handleChargerChange(connected) },
                onHeadphonesChanged = { connected -> handleHeadphonesChange(connected) }
            )
            systemTrigger.register()
            systemTriggerManager = systemTrigger
            FileLogManager.logWarning(this, "ShortcutAccessibilityService", "Monitores de eventos del sistema registrados en segundo plano.")
        } catch (e: Exception) {
            FileLogManager.logWarning(this, "ShortcutAccessibilityService", "Error registrando monitores: ${e.localizedMessage}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            systemTriggerManager?.unregister()
            systemTriggerManager = null
        } catch (e: Exception) {
            // Ignore
        }
        if (instance == this) {
            instance = null
        }
    }

    private fun handleBatteryChange(percentage: Int) {
        val previousLevel = lastSeenBatteryLevel
        if (previousLevel == percentage) return
        lastSeenBatteryLevel = percentage
        triggeredShortcutsForLevel.clear()

        serviceScope.launch {
            try {
                val db = com.example.data.ShortcutDatabase.getDatabase(applicationContext)
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
                // Ignore
            }
        }
    }

    private fun handleChargerChange(connected: Boolean) {
        if (lastSeenChargerState == connected) return
        lastSeenChargerState = connected

        serviceScope.launch {
            try {
                val db = com.example.data.ShortcutDatabase.getDatabase(applicationContext)
                val shortcuts = db.shortcutDao().getAllShortcutsList()
                shortcuts.forEach { shortcut ->
                    if (shortcut.isChargerTriggerEnabled) {
                        val expectedType = shortcut.triggerChargerType ?: "CONNECTED"
                        val shouldTrigger = (expectedType == "CONNECTED" && connected) || 
                                            (expectedType == "DISCONNECTED" && !connected)
                        if (shouldTrigger) {
                            BackgroundExecutor.executeShortcutInBackground(applicationContext, shortcut)
                        }
                    }
                }
            } catch (e: Exception) {
                // Ignore
            }
        }
    }

    private fun handleHeadphonesChange(connected: Boolean) {
        if (lastSeenHeadphonesState == connected) return
        lastSeenHeadphonesState = connected

        serviceScope.launch {
            try {
                val db = com.example.data.ShortcutDatabase.getDatabase(applicationContext)
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
                // Ignore
            }
        }
    }

    companion object {
        @Volatile
        private var instance: ShortcutAccessibilityService? = null

        fun isServiceRunning(): Boolean {
            return instance != null
        }

        fun performGlobalAction(actionId: Int): Boolean {
            val service = instance ?: return false
            return service.performGlobalAction(actionId)
        }

        suspend fun simulateGesture(path: android.graphics.Path, durationMs: Long): Boolean {
            val service = instance ?: return false
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                val deferred = kotlinx.coroutines.CompletableDeferred<Boolean>()
                val stroke = android.accessibilityservice.GestureDescription.StrokeDescription(path, 0, durationMs)
                val gestureDescription = android.accessibilityservice.GestureDescription.Builder()
                    .addStroke(stroke)
                    .build()
                
                val success = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                    service.dispatchGesture(gestureDescription, object : android.accessibilityservice.AccessibilityService.GestureResultCallback() {
                        override fun onCompleted(gestureDescription: android.accessibilityservice.GestureDescription?) {
                            deferred.complete(true)
                        }
                        override fun onCancelled(gestureDescription: android.accessibilityservice.GestureDescription?) {
                            deferred.complete(false)
                        }
                    }, null)
                }
                
                if (!success) {
                    return false
                }
                return try {
                    deferred.await()
                } catch (e: Exception) {
                    false
                }
            }
            return false
        }
    }
}

package com.example.ui

import com.example.data.Shortcut
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ShortcutAutomationHandler(
    private val allShortcutsProvider: () -> List<Shortcut>,
    private val runShortcut: (Shortcut) -> Unit,
    private val addLog: (String) -> Unit,
    private val coroutineScope: CoroutineScope
) {
    private var lastSeenBatteryLevel: Int? = null
    private val triggeredShortcutsForLevel = mutableSetOf<Int>()
    private var lastSeenChargerState: Boolean? = null
    private var lastSeenHeadphonesState: Boolean? = null

    // Robust Cooldown to prevent multi-firing / bouncing hardware states
    private val lastTriggeredTimes = mutableMapOf<String, Long>()
    private val COOLDOWN_MS = 4000L

    fun handleBatteryChange(percentage: Int) {
        val previousLevel = lastSeenBatteryLevel
        if (previousLevel == percentage) return // No level change

        lastSeenBatteryLevel = percentage
        triggeredShortcutsForLevel.clear() // Reset triggered ids when level shifts

        val now = System.currentTimeMillis()
        val currentList = allShortcutsProvider()
        coroutineScope.launch {
            currentList.forEach { shortcut ->
                if (shortcut.isBatteryTriggerEnabled && shortcut.triggerBatteryLevel == percentage) {
                    val type = shortcut.triggerBatteryType ?: "EQUALS"
                    var shouldTrigger = false

                    when (type) {
                        "EQUALS" -> {
                            shouldTrigger = true
                        }
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
                        val key = "battery_${shortcut.id}_$percentage"
                        val lastTime = lastTriggeredTimes[key] ?: 0L
                        if (now - lastTime >= COOLDOWN_MS) {
                            lastTriggeredTimes[key] = now
                            triggeredShortcutsForLevel.add(shortcut.id)
                            addLog("⚡ [Automatización] Batería al $percentage%. Iniciando: \"${shortcut.name}\".")
                            try {
                                runShortcut(shortcut)
                            } catch (e: Exception) {
                                addLog("❌ Error iniciando atajo por batería: ${e.localizedMessage}")
                            }
                        }
                    }
                }
            }
        }
    }

    fun handleChargerChange(connected: Boolean) {
        if (lastSeenChargerState == connected) return
        lastSeenChargerState = connected

        val now = System.currentTimeMillis()
        val currentList = allShortcutsProvider()
        coroutineScope.launch {
            currentList.forEach { shortcut ->
                if (shortcut.isChargerTriggerEnabled) {
                    val expectedType = shortcut.triggerChargerType ?: "CONNECTED"
                    val shouldTrigger = (expectedType == "CONNECTED" && connected) || 
                                        (expectedType == "DISCONNECTED" && !connected)
                    if (shouldTrigger) {
                        val key = "charger_${shortcut.id}_$expectedType"
                        val lastTime = lastTriggeredTimes[key] ?: 0L
                        if (now - lastTime >= COOLDOWN_MS) {
                            lastTriggeredTimes[key] = now
                            val stateStr = if (connected) "conectado" else "desconectado"
                            addLog("⚡ [Automatización] Cargador $stateStr. Iniciando: \"${shortcut.name}\".")
                            try {
                                runShortcut(shortcut)
                            } catch (e: Exception) {
                                addLog("❌ Error iniciando atajo por cargador: ${e.localizedMessage}")
                            }
                        }
                    }
                }
            }
        }
    }

    fun handleHeadphonesChange(connected: Boolean) {
        if (lastSeenHeadphonesState == connected) return
        lastSeenHeadphonesState = connected

        val now = System.currentTimeMillis()
        val currentList = allShortcutsProvider()
        coroutineScope.launch {
            currentList.forEach { shortcut ->
                if (shortcut.isHeadphonesTriggerEnabled) {
                    val expectedType = shortcut.triggerHeadphonesType ?: "CONNECTED"
                    val shouldTrigger = (expectedType == "CONNECTED" && connected) || 
                                        (expectedType == "DISCONNECTED" && !connected)
                    if (shouldTrigger) {
                        val key = "headphones_${shortcut.id}_$expectedType"
                        val lastTime = lastTriggeredTimes[key] ?: 0L
                        if (now - lastTime >= COOLDOWN_MS) {
                            lastTriggeredTimes[key] = now
                            val stateStr = if (connected) "conectados" else "desconectados"
                            addLog("⚡ [Automatización] Auriculares $stateStr. Iniciando: \"${shortcut.name}\".")
                            try {
                                runShortcut(shortcut)
                            } catch (e: Exception) {
                                addLog("❌ Error iniciando atajo por auriculares: ${e.localizedMessage}")
                            }
                        }
                    }
                }
            }
        }
    }
}

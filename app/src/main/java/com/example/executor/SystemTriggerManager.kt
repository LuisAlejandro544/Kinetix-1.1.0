package com.example.executor

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build

class SystemTriggerManager(
    context: Context,
    private val onBatteryLevelChanged: (percentage: Int) -> Unit,
    private val onChargerChanged: (connected: Boolean) -> Unit,
    private val onHeadphonesChanged: (connected: Boolean) -> Unit
) {
    // Keep reference to applicationContext to avoid memory leaks
    private val appContext = context.applicationContext
    private var isRegistered = false

    private val triggerReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctx: Context, intent: Intent) {
            val action = intent.action ?: return
            when (action) {
                Intent.ACTION_BATTERY_CHANGED -> {
                    val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                    val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                    if (level != -1 && scale != -1) {
                        val percentage = (level * 100 / scale.toFloat()).toInt()
                        onBatteryLevelChanged(percentage)
                    }
                }
                Intent.ACTION_POWER_CONNECTED -> {
                    onChargerChanged(true)
                }
                Intent.ACTION_POWER_DISCONNECTED -> {
                    onChargerChanged(false)
                }
                Intent.ACTION_HEADSET_PLUG -> {
                    val state = intent.getIntExtra("state", -1)
                    if (state == 1) {
                        onHeadphonesChanged(true)
                    } else if (state == 0) {
                        onHeadphonesChanged(false)
                    }
                }
            }
        }
    }

    fun register() {
        if (isRegistered) return
        try {
            val filter = IntentFilter().apply {
                addAction(Intent.ACTION_BATTERY_CHANGED)
                addAction(Intent.ACTION_POWER_CONNECTED)
                addAction(Intent.ACTION_POWER_DISCONNECTED)
                addAction(Intent.ACTION_HEADSET_PLUG)
            }
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // System actions are sent by Android. Standard actions require export flag.
                // We use RECEIVER_EXPORTED as power/headset can be generic, or RECEIVER_NOT_EXPORTED for high security.
                // RECEIVER_NOT_EXPORTED is safer and compatible with system broadcasts like battery/headset.
                appContext.registerReceiver(triggerReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
            } else {
                appContext.registerReceiver(triggerReceiver, filter)
            }
            isRegistered = true
            FileLogManager.logWarning(appContext, "SystemTriggerManager", "✅ Triggers del sistema registrados exitosamente.")
        } catch (e: Exception) {
            FileLogManager.logWarning(appContext, "SystemTriggerManager", "❌ Error registrando triggers: ${e.localizedMessage}")
        }
    }

    fun unregister() {
        if (!isRegistered) return
        try {
            appContext.unregisterReceiver(triggerReceiver)
            isRegistered = false
            FileLogManager.logWarning(appContext, "SystemTriggerManager", "✅ Triggers del sistema desregistrados.")
        } catch (e: Exception) {
            // Ignore
        }
    }
}

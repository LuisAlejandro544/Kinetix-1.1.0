package com.example.executor.strategies

import com.example.executor.ActionStrategy

object SystemStrategiesRegistry {
    val strategies: Map<com.example.data.ActionType, ActionStrategy> = mapOf(
        com.example.data.ActionType.VIBRATE to VibrateStrategy(),
        com.example.data.ActionType.SYSTEM_INFO to SystemInfoStrategy(),
        com.example.data.ActionType.SET_BRIGHTNESS to SetBrightnessStrategy(),
        com.example.data.ActionType.SET_RINGER_MODE to SetRingerModeStrategy(),
        com.example.data.ActionType.ACCESSIBILITY_ACTION to AccessibilityActionStrategy(),
        com.example.data.ActionType.OPEN_APP to OpenAppStrategy(),
        com.example.data.ActionType.OPEN_URL to OpenUrlStrategy()
    )
}

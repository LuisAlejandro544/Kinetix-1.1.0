package com.example.executor.strategies

import com.example.executor.ActionStrategy

object AudioVisualStrategiesRegistry {
    val strategies: Map<com.example.data.ActionType, ActionStrategy> = mapOf(
        com.example.data.ActionType.SPEAK_TEXT to SpeakTextStrategy(),
        com.example.data.ActionType.SHOW_NOTIFICATION to ShowNotificationStrategy(),
        com.example.data.ActionType.PLAY_SOUND to PlaySoundStrategy(),
        com.example.data.ActionType.SET_VOLUME to SetVolumeStrategy()
    )
}

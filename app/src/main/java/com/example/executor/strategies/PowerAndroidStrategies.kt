package com.example.executor.strategies

import com.example.executor.ActionStrategy

object PowerAndroidStrategiesRegistry {
    val strategies: Map<com.example.data.ActionType, ActionStrategy> = mapOf(
        com.example.data.ActionType.BACKGROUND_CAMERA_CAPTURE to BackgroundCameraCaptureStrategy(),
        com.example.data.ActionType.SIMULATE_GESTURES to SimulateGesturesStrategy(),
        com.example.data.ActionType.CLIPBOARD_SILENT to ClipboardStrategy()
    )
}

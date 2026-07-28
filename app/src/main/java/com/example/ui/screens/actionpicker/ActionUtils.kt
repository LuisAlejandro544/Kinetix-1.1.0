package com.example.ui.screens.actionpicker

import com.example.data.ActionType

fun getCategoryForAction(type: ActionType): String {
    return when (type) {
        ActionType.SPEAK_TEXT,
        ActionType.PLAY_SOUND,
        ActionType.SET_VOLUME,
        ActionType.SET_RINGER_MODE -> "Voz y Sonido"

        ActionType.SHOW_NOTIFICATION,
        ActionType.SYSTEM_INFO,
        ActionType.VIBRATE,
        ActionType.SET_BRIGHTNESS,
        ActionType.OPEN_APP,
        ActionType.OPEN_URL,
        ActionType.ACCESSIBILITY_ACTION,
        ActionType.SIMULATE_GESTURES -> "Sistema"

        ActionType.WRITE_FILE,
        ActionType.READ_FILE,
        ActionType.APPEND_FILE,
        ActionType.CLIPBOARD_SILENT,
        ActionType.SHARE_TEXT,
        ActionType.TEXT_TRANSFORM -> "Archivos y Datos"

        ActionType.CONDITIONAL,
        ActionType.ALERT_DIALOG,
        ActionType.TEXT_INPUT,
        ActionType.MATH_OP,
        ActionType.EXEC_JAVASCRIPT,
        ActionType.CUSTOM_CODE,
        ActionType.TERMUX_COMMAND,
        ActionType.BACKGROUND_CAMERA_CAPTURE,
        ActionType.HTTP_REQUEST -> "Lógica y Código"
    }
}

fun requiresAccessibility(type: ActionType): Boolean {
    return type == ActionType.ACCESSIBILITY_ACTION || type == ActionType.SIMULATE_GESTURES
}

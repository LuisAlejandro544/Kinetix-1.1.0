package com.example.data

enum class ActionCategory(val displayName: String) {
    AUDIO_VISUAL("Audio y Visual"),
    SYSTEM("Información y Sistema"),
    FILES("Archivos y Almacenamiento"),
    LOGIC("Lógica y Flujo"),
    POWER_ANDROID("Hardware y Accesibilidad"),
    DEVELOPER("Programación y Avanzado")
}

fun ActionType.getCategory(): ActionCategory {
    return when (this) {
        ActionType.SPEAK_TEXT,
        ActionType.SHOW_NOTIFICATION,
        ActionType.PLAY_SOUND,
        ActionType.SET_VOLUME,
        ActionType.SET_RINGER_MODE -> ActionCategory.AUDIO_VISUAL

        ActionType.SYSTEM_INFO,
        ActionType.OPEN_URL,
        ActionType.OPEN_APP,
        ActionType.SET_BRIGHTNESS -> ActionCategory.SYSTEM

        ActionType.WRITE_FILE,
        ActionType.READ_FILE,
        ActionType.APPEND_FILE -> ActionCategory.FILES

        ActionType.TEXT_INPUT,
        ActionType.TEXT_TRANSFORM,
        ActionType.ALERT_DIALOG,
        ActionType.MATH_OP,
        ActionType.SHARE_TEXT,
        ActionType.CONDITIONAL -> ActionCategory.LOGIC

        ActionType.VIBRATE,
        ActionType.ACCESSIBILITY_ACTION,
        ActionType.BACKGROUND_CAMERA_CAPTURE,
        ActionType.SIMULATE_GESTURES,
        ActionType.CLIPBOARD_SILENT -> ActionCategory.POWER_ANDROID

        ActionType.EXEC_JAVASCRIPT,
        ActionType.TERMUX_COMMAND,
        ActionType.CUSTOM_CODE,
        ActionType.HTTP_REQUEST -> ActionCategory.DEVELOPER
    }
}

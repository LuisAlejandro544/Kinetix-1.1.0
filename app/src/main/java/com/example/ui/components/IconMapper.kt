package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

fun getIconByName(name: String): ImageVector {
    return when (name) {
        "battery" -> Icons.Default.BatteryChargingFull
        "volume" -> Icons.Default.VolumeUp
        "share" -> Icons.Default.Share
        "calculator" -> Icons.Default.Calculate
        "star" -> Icons.Default.Star
        "phone" -> Icons.Default.Phone
        "message" -> Icons.Default.ChatBubble
        "music" -> Icons.Default.MusicNote
        "home" -> Icons.Default.Home
        "location" -> Icons.Default.LocationOn
        "alarm" -> Icons.Default.Alarm
        "code" -> Icons.Default.Code
        "light" -> Icons.Default.Lightbulb
        "camera" -> Icons.Default.CameraAlt
        else -> Icons.Default.PlayArrow
    }
}

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
        "wifi" -> Icons.Default.Wifi
        "bluetooth" -> Icons.Default.Bluetooth
        "settings" -> Icons.Default.Settings
        "lock" -> Icons.Default.Lock
        "key" -> Icons.Default.Key
        "timer" -> Icons.Default.Timer
        "folder" -> Icons.Default.Folder
        "flash" -> Icons.Default.FlashOn
        "download" -> Icons.Default.Download
        "cloud" -> Icons.Default.Cloud
        "shield" -> Icons.Default.Shield
        "terminal" -> Icons.Default.Terminal
        "power" -> Icons.Default.PowerSettingsNew
        "tune" -> Icons.Default.Tune
        "palette" -> Icons.Default.Palette
        "mic" -> Icons.Default.Mic
        "globe" -> Icons.Default.Language
        "heart" -> Icons.Default.Favorite
        "bolt" -> Icons.Default.Bolt
        "notifications" -> Icons.Default.Notifications
        "brightness" -> Icons.Default.Brightness6
        "build" -> Icons.Default.Build
        "game" -> Icons.Default.SportsEsports
        "movie" -> Icons.Default.Movie
        "shopping" -> Icons.Default.ShoppingCart
        "android" -> Icons.Default.Android
        else -> Icons.Default.PlayArrow
    }
}

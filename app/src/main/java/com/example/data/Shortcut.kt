package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shortcuts")
data class Shortcut(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val iconName: String,
    val colorHex: String,
    val actions: List<ActionData> = emptyList(),
    val createdTimestamp: Long = System.currentTimeMillis(),
    val customPhotoUri: String? = null,
    val isBatteryTriggerEnabled: Boolean = false,
    val triggerBatteryLevel: Int? = null,
    val triggerBatteryType: String? = null, // "EQUALS", "FALLS_BELOW", "RISES_ABOVE"
    val isChargerTriggerEnabled: Boolean = false,
    val triggerChargerType: String? = null, // "CONNECTED", "DISCONNECTED"
    val isHeadphonesTriggerEnabled: Boolean = false,
    val triggerHeadphonesType: String? = null, // "CONNECTED", "DISCONNECTED"
    val isScheduleTriggerEnabled: Boolean = false,
    val triggerScheduleTime: String? = null, // "08:30"
    val triggerScheduleDays: String? = null // "DAILY", "MON,TUE,WED,THU,FRI", etc.
)

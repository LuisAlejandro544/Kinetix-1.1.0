package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.screens.identity.*

@Composable
fun ShortcutIdentityCard(
    name: String,
    onNameChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    colorHex: String,
    onColorHexChange: (String) -> Unit,
    iconName: String,
    onIconNameChange: (String) -> Unit,
    customPhotoUri: String?,
    onCustomPhotoUriChange: (String?) -> Unit,
    isBatteryTriggerEnabled: Boolean,
    onBatteryTriggerEnabledChange: (Boolean) -> Unit,
    triggerBatteryLevel: Int,
    onBatteryLevelChange: (Int) -> Unit,
    triggerBatteryType: String,
    onBatteryTypeChange: (String) -> Unit,
    isChargerTriggerEnabled: Boolean,
    onChargerTriggerEnabledChange: (Boolean) -> Unit,
    triggerChargerType: String,
    onChargerTypeChange: (String) -> Unit,
    isHeadphonesTriggerEnabled: Boolean,
    onHeadphonesTriggerEnabledChange: (Boolean) -> Unit,
    triggerHeadphonesType: String,
    onHeadphonesTypeChange: (String) -> Unit,
    isScheduleTriggerEnabled: Boolean = false,
    onScheduleTriggerEnabledChange: (Boolean) -> Unit = {},
    triggerScheduleTime: String = "08:30",
    onScheduleTimeChange: (String) -> Unit = {},
    triggerScheduleDays: String = "DAILY",
    onScheduleDaysChange: (String) -> Unit = {},
    isNotificationTriggerEnabled: Boolean = false,
    onNotificationTriggerEnabledChange: (Boolean) -> Unit = {},
    triggerNotificationApp: String = "",
    onNotificationAppChange: (String) -> Unit = {},
    triggerNotificationKeyword: String = "",
    onNotificationKeywordChange: (String) -> Unit = {},
    colorPalette: List<String>,
    iconPalette: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Name Input
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text("Nombre del Atajo") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("shortcut_name_input"),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Description Input
            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                label = { Text("Descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("shortcut_desc_input"),
                singleLine = false,
                maxLines = 2,
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Color Selector
            ColorSelector(
                colorHex = colorHex,
                onColorHexChange = onColorHexChange,
                colorPalette = colorPalette
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Icon Selector
            IconSelector(
                iconName = iconName,
                onIconNameChange = onIconNameChange,
                iconPalette = iconPalette
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Custom Photo Section
            CustomPhotoSection(
                customPhotoUri = customPhotoUri,
                onCustomPhotoUriChange = onCustomPhotoUriChange
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Battery Trigger Section
            BatteryTriggerSection(
                isBatteryTriggerEnabled = isBatteryTriggerEnabled,
                onBatteryTriggerEnabledChange = onBatteryTriggerEnabledChange,
                triggerBatteryLevel = triggerBatteryLevel,
                onBatteryLevelChange = onBatteryLevelChange,
                triggerBatteryType = triggerBatteryType,
                onBatteryTypeChange = onBatteryTypeChange
            )

            // Charger Trigger Section
            ChargerTriggerSection(
                isChargerTriggerEnabled = isChargerTriggerEnabled,
                onChargerTriggerEnabledChange = onChargerTriggerEnabledChange,
                triggerChargerType = triggerChargerType,
                onChargerTypeChange = onChargerTypeChange
            )

            // Headphones Trigger Section
            HeadphonesTriggerSection(
                isHeadphonesTriggerEnabled = isHeadphonesTriggerEnabled,
                onHeadphonesTriggerEnabledChange = onHeadphonesTriggerEnabledChange,
                triggerHeadphonesType = triggerHeadphonesType,
                onHeadphonesTypeChange = onHeadphonesTypeChange
            )

            // Schedule Trigger Section
            ScheduleTriggerSection(
                isScheduleTriggerEnabled = isScheduleTriggerEnabled,
                onScheduleTriggerEnabledChange = onScheduleTriggerEnabledChange,
                triggerScheduleTime = triggerScheduleTime,
                onScheduleTimeChange = onScheduleTimeChange,
                triggerScheduleDays = triggerScheduleDays,
                onScheduleDaysChange = onScheduleDaysChange
            )

            // Notification Trigger Section
            NotificationTriggerSection(
                isNotificationTriggerEnabled = isNotificationTriggerEnabled,
                onNotificationTriggerEnabledChange = onNotificationTriggerEnabledChange,
                triggerNotificationApp = triggerNotificationApp,
                onNotificationAppChange = onNotificationAppChange,
                triggerNotificationKeyword = triggerNotificationKeyword,
                onNotificationKeywordChange = onNotificationKeywordChange
            )
        }
    }
}

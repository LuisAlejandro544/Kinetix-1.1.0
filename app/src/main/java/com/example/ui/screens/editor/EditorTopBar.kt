package com.example.ui.screens.editor

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.ActionData
import com.example.data.Shortcut
import com.example.ui.ShortcutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorTopBar(
    name: String,
    description: String,
    iconName: String,
    colorHex: String,
    actionsList: List<ActionData>,
    customPhotoUri: String?,
    isBatteryTriggerEnabled: Boolean,
    triggerBatteryLevel: Int,
    triggerBatteryType: String,
    isChargerTriggerEnabled: Boolean,
    triggerChargerType: String,
    isHeadphonesTriggerEnabled: Boolean,
    triggerHeadphonesType: String,
    draftShortcut: Shortcut,
    viewModel: ShortcutViewModel,
    onNavigateBack: () -> Unit
) {
    TopAppBar(
        title = { Text("Diseño del Atajo", fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
            }
        },
        actions = {
            // Test Run Shortcut on the fly!
            IconButton(
                onClick = {
                    val savedShortcut = Shortcut(
                        id = draftShortcut.id,
                        name = name,
                        description = description,
                        iconName = iconName,
                        colorHex = colorHex,
                        actions = actionsList,
                        customPhotoUri = customPhotoUri,
                        isBatteryTriggerEnabled = isBatteryTriggerEnabled,
                        triggerBatteryLevel = triggerBatteryLevel,
                        triggerBatteryType = triggerBatteryType,
                        isChargerTriggerEnabled = isChargerTriggerEnabled,
                        triggerChargerType = triggerChargerType,
                        isHeadphonesTriggerEnabled = isHeadphonesTriggerEnabled,
                        triggerHeadphonesType = triggerHeadphonesType
                    )
                    viewModel.saveShortcut(savedShortcut)
                    viewModel.runShortcut(savedShortcut)
                },
                modifier = Modifier.testTag("test_run_editor_button")
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Test Atajo", tint = Color(0xFF34D399))
            }

            // Save Shortcut
            Button(
                onClick = {
                    val savedShortcut = Shortcut(
                        id = draftShortcut.id,
                        name = name,
                        description = description,
                        iconName = iconName,
                        colorHex = colorHex,
                        actions = actionsList,
                        customPhotoUri = customPhotoUri,
                        isBatteryTriggerEnabled = isBatteryTriggerEnabled,
                        triggerBatteryLevel = triggerBatteryLevel,
                        triggerBatteryType = triggerBatteryType,
                        isChargerTriggerEnabled = isChargerTriggerEnabled,
                        triggerChargerType = triggerChargerType,
                        isHeadphonesTriggerEnabled = isHeadphonesTriggerEnabled,
                        triggerHeadphonesType = triggerHeadphonesType
                    )
                    viewModel.saveShortcut(savedShortcut)
                    onNavigateBack()
                },
                modifier = Modifier
                    .padding(end = 8.dp)
                    .testTag("save_shortcut_button"),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Guardar", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    )
}

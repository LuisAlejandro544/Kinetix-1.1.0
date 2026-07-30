package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ActionData
import com.example.data.ActionType
import com.example.data.Shortcut
import com.example.ui.ShortcutViewModel
import com.example.ui.components.*
import com.example.ui.screens.editor.EditorTopBar
import com.example.ui.screens.editor.EmptySequenceCard
import com.example.ui.screens.editor.ActionSequenceSection
import com.example.ui.screens.editor.EditorPalettes
import com.example.ui.screens.editor.AddActionButton
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShortcutEditorScreen(
    viewModel: ShortcutViewModel,
    onNavigateBack: () -> Unit
) {
    val draftShortcut = viewModel.selectedShortcut ?: return

    // Local mutable state for editing before saving
    var name by remember { mutableStateOf(draftShortcut.name) }
    var description by remember { mutableStateOf(draftShortcut.description) }
    var colorHex by remember { mutableStateOf(draftShortcut.colorHex) }
    var iconName by remember { mutableStateOf(draftShortcut.iconName) }
    var actionsList by remember { mutableStateOf(draftShortcut.actions) }
    var customPhotoUri by remember { mutableStateOf(draftShortcut.customPhotoUri) }
    
    // Trigger States
    var isBatteryTriggerEnabled by remember { mutableStateOf(draftShortcut.isBatteryTriggerEnabled) }
    var triggerBatteryLevel by remember { mutableStateOf(draftShortcut.triggerBatteryLevel ?: 20) }
    var triggerBatteryType by remember { mutableStateOf(draftShortcut.triggerBatteryType ?: "EQUALS") }

    var isChargerTriggerEnabled by remember { mutableStateOf(draftShortcut.isChargerTriggerEnabled) }
    var triggerChargerType by remember { mutableStateOf(draftShortcut.triggerChargerType ?: "CONNECTED") }

    var isHeadphonesTriggerEnabled by remember { mutableStateOf(draftShortcut.isHeadphonesTriggerEnabled) }
    var triggerHeadphonesType by remember { mutableStateOf(draftShortcut.triggerHeadphonesType ?: "CONNECTED") }

    var isScheduleTriggerEnabled by remember { mutableStateOf(draftShortcut.isScheduleTriggerEnabled) }
    var triggerScheduleTime by remember { mutableStateOf(draftShortcut.triggerScheduleTime ?: "08:30") }
    var triggerScheduleDays by remember { mutableStateOf(draftShortcut.triggerScheduleDays ?: "DAILY") }

    var isNotificationTriggerEnabled by remember { mutableStateOf(draftShortcut.isNotificationTriggerEnabled) }
    var triggerNotificationApp by remember { mutableStateOf(draftShortcut.triggerNotificationApp ?: "") }
    var triggerNotificationKeyword by remember { mutableStateOf(draftShortcut.triggerNotificationKeyword ?: "") }

    var showActionPickerSheet by remember { mutableStateOf(false) }

    val colorPalette = EditorPalettes.colorPalette
    val iconPalette = EditorPalettes.iconPalette

    Scaffold(
        topBar = {
            EditorTopBar(
                name = name,
                description = description,
                iconName = iconName,
                colorHex = colorHex,
                actionsList = actionsList,
                customPhotoUri = customPhotoUri,
                isBatteryTriggerEnabled = isBatteryTriggerEnabled,
                triggerBatteryLevel = triggerBatteryLevel,
                triggerBatteryType = triggerBatteryType,
                isChargerTriggerEnabled = isChargerTriggerEnabled,
                triggerChargerType = triggerChargerType,
                isHeadphonesTriggerEnabled = isHeadphonesTriggerEnabled,
                triggerHeadphonesType = triggerHeadphonesType,
                isScheduleTriggerEnabled = isScheduleTriggerEnabled,
                triggerScheduleTime = triggerScheduleTime,
                triggerScheduleDays = triggerScheduleDays,
                isNotificationTriggerEnabled = isNotificationTriggerEnabled,
                triggerNotificationApp = triggerNotificationApp,
                triggerNotificationKeyword = triggerNotificationKeyword,
                draftShortcut = draftShortcut,
                viewModel = viewModel,
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                // Section 1: Visual and Identity Styling
                item {
                    Text(
                        "Identidad Visual",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    ShortcutIdentityCard(
                        name = name,
                        onNameChange = { name = it },
                        description = description,
                        onDescriptionChange = { description = it },
                        colorHex = colorHex,
                        onColorHexChange = { colorHex = it },
                        iconName = iconName,
                        onIconNameChange = { iconName = it },
                        customPhotoUri = customPhotoUri,
                        onCustomPhotoUriChange = { customPhotoUri = it },
                        isBatteryTriggerEnabled = isBatteryTriggerEnabled,
                        onBatteryTriggerEnabledChange = { isBatteryTriggerEnabled = it },
                        triggerBatteryLevel = triggerBatteryLevel,
                        onBatteryLevelChange = { triggerBatteryLevel = it },
                        triggerBatteryType = triggerBatteryType,
                        onBatteryTypeChange = { triggerBatteryType = it },
                        isChargerTriggerEnabled = isChargerTriggerEnabled,
                        onChargerTriggerEnabledChange = { isChargerTriggerEnabled = it },
                        triggerChargerType = triggerChargerType,
                        onChargerTypeChange = { triggerChargerType = it },
                        isHeadphonesTriggerEnabled = isHeadphonesTriggerEnabled,
                        onHeadphonesTriggerEnabledChange = { isHeadphonesTriggerEnabled = it },
                        triggerHeadphonesType = triggerHeadphonesType,
                        onHeadphonesTypeChange = { triggerHeadphonesType = it },
                        isScheduleTriggerEnabled = isScheduleTriggerEnabled,
                        onScheduleTriggerEnabledChange = { isScheduleTriggerEnabled = it },
                        triggerScheduleTime = triggerScheduleTime,
                        onScheduleTimeChange = { triggerScheduleTime = it },
                        triggerScheduleDays = triggerScheduleDays,
                        onScheduleDaysChange = { triggerScheduleDays = it },
                        isNotificationTriggerEnabled = isNotificationTriggerEnabled,
                        onNotificationTriggerEnabledChange = { isNotificationTriggerEnabled = it },
                        triggerNotificationApp = triggerNotificationApp,
                        onNotificationAppChange = { triggerNotificationApp = it },
                        triggerNotificationKeyword = triggerNotificationKeyword,
                        onNotificationKeywordChange = { triggerNotificationKeyword = it },
                        colorPalette = colorPalette,
                        iconPalette = iconPalette
                    )
                }

                // Section 2: Flow steps
                item {
                    ActionSequenceSection(
                        actionsList = actionsList,
                        onActionsListChange = { actionsList = it }
                    )
                }

                // Add action button trigger
                item {
                    AddActionButton(
                        onClick = { showActionPickerSheet = true }
                    )
                }
            }
        }
    }

    // Modal Sheet or Dialog for Picking actions
    if (showActionPickerSheet) {
        ActionPickerDial(
            onDismiss = { showActionPickerSheet = false },
            onActionSelected = { selectedType ->
                actionsList = actionsList + ActionData(
                    type = selectedType,
                    params = selectedType.defaultParams
                )
                showActionPickerSheet = false
            }
        )
    }

    // Connect to view model consoles in editor as well for seamless testing!
    if (viewModel.showConsole) {
        ConsoleDrawer(viewModel = viewModel)
    }
}

package com.example.ui.screens.list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShortcutListTopBar(
    onOpenTutorial: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                "Kinetix",
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )
        },
        actions = {
            IconButton(
                onClick = onOpenTutorial,
                modifier = Modifier.testTag("tutorial_button")
            ) {
                Icon(
                    imageVector = Icons.Default.HelpOutline,
                    contentDescription = "Manual de Variables",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(
                onClick = onNavigateToSettings,
                modifier = Modifier.testTag("settings_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Configuración de Kinetix",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

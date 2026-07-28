package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Shortcut
import com.example.ui.ShortcutViewModel
import com.example.ui.components.*
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.example.ui.screens.list.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShortcutListScreen(
    viewModel: ShortcutViewModel,
    onNavigateToEditor: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val context = LocalContext.current
    val shortcuts by viewModel.allShortcuts.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    var isAccessibilityActive by remember {
        mutableStateOf(com.example.executor.ShortcutAccessibilityService.isServiceRunning())
    }

    var showTutorialScreen by remember { mutableStateOf(false) }
    var isTutorialForCreation by remember { mutableStateOf(false) }
    var showAccessibilityWarningDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            isAccessibilityActive = com.example.executor.ShortcutAccessibilityService.isServiceRunning()
            kotlinx.coroutines.delay(2000)
        }
    }

    val filteredShortcuts = remember(shortcuts, searchQuery) {
        if (searchQuery.isBlank()) {
            shortcuts
        } else {
            shortcuts.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                it.description.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Scaffold(
        topBar = {
            ShortcutListTopBar(
                onOpenTutorial = {
                    isTutorialForCreation = false
                    showTutorialScreen = true
                },
                onNavigateToSettings = onNavigateToSettings
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val prefs = context.getSharedPreferences("kinetix_prefs", android.content.Context.MODE_PRIVATE)
                    val isTutorialCompleted = prefs.getBoolean("flow_tutorial_completed", false)
                    if (!isTutorialCompleted) {
                        isTutorialForCreation = true
                        showTutorialScreen = true
                    } else {
                        // Create a new empty shortcut with nice default values
                        val newShortcut = Shortcut(
                            name = "Nueva Automatización",
                            description = "Crea una secuencia de acciones para automatizar tu día.",
                            iconName = "star",
                            colorHex = "#007AFF", // Blue
                            actions = emptyList()
                        )
                        viewModel.selectShortcutForEditing(newShortcut)
                        onNavigateToEditor()
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.testTag("add_shortcut_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear Atajo")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar atajos...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .testTag("search_shortcuts_input"),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Intro Banner (Decorative / Hero UX)
            if (shortcuts.isNotEmpty() && searchQuery.isEmpty()) {
                IntroBanner(isLowGraphicsQuality = viewModel.isLowGraphicsQuality)
            }

            if (!isAccessibilityActive && searchQuery.isEmpty()) {
                AccessibilityWarningBanner(onClick = { showAccessibilityWarningDialog = true })
            }

            if (filteredShortcuts.isEmpty()) {
                EmptyStateView(
                    searchQuery = searchQuery,
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredShortcuts, key = { it.id }) { shortcut ->
                        ShortcutGridItem(
                            shortcut = shortcut,
                            isLowGraphicsQuality = viewModel.isLowGraphicsQuality,
                            onRun = { viewModel.runShortcut(shortcut) },
                            onClick = {
                                viewModel.selectShortcutForEditing(shortcut)
                                onNavigateToEditor()
                            },
                            onDelete = { viewModel.deleteShortcut(shortcut) }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            ShortcutListFooter()
            }

            // Beautiful floating progress indicator capsule at the bottom
            ExecutingProgressCapsule(
                isExecuting = viewModel.isExecuting,
                activeStepName = viewModel.activeStepName,
                currentStep = viewModel.currentStep,
                totalSteps = viewModel.totalSteps,
                onClick = { viewModel.showConsole = true }
            )
        }
    }

    if (showTutorialScreen) {
        VariablesTutorialScreen(
            onDismiss = { showTutorialScreen = false },
            onComplete = {
                val prefs = context.getSharedPreferences("kinetix_prefs", android.content.Context.MODE_PRIVATE)
                prefs.edit().putBoolean("flow_tutorial_completed", true).apply()
                showTutorialScreen = false
                if (isTutorialForCreation) {
                    val newShortcut = Shortcut(
                        name = "Nueva Automatización",
                        description = "Crea una secuencia de acciones para automatizar tu día.",
                        iconName = "star",
                        colorHex = "#007AFF", // Blue
                        actions = emptyList()
                    )
                    viewModel.selectShortcutForEditing(newShortcut)
                    onNavigateToEditor()
                }
            }
        )
    }

    if (showAccessibilityWarningDialog) {
        AccessibilityWarningDialog(
            context = context,
            onDismiss = { showAccessibilityWarningDialog = false }
        )
    }

    // Interactive Dialog Overlays (Suspended Execution States)
    InteractiveDialogs(viewModel = viewModel)

    // Bottom Sheet Log Console Overlay
    if (viewModel.showConsole) {
        ConsoleDrawer(viewModel = viewModel)
    }
}

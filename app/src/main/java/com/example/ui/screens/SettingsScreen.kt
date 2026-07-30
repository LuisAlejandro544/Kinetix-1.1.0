package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.executor.FileLog
import com.example.executor.FileLogManager
import com.example.ui.ShortcutViewModel
import com.example.ui.screens.settings.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(

    viewModel: ShortcutViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current
    var isLowGraphics by remember { mutableStateOf(viewModel.isLowGraphicsQuality) }
    
    // Log lists state
    var crashLogs by remember { mutableStateOf<List<FileLog>>(emptyList()) }
    var warningLogs by remember { mutableStateOf<List<FileLog>>(emptyList()) }
    var selectedLog by remember { mutableStateOf<FileLog?>(null) }
    var logFilter by remember { mutableStateOf("ALL") } // "ALL", "CRASH", "WARNING"

    // Refresh logs function
    val refreshLogs = {
        crashLogs = FileLogManager.getCrashLogs(context)
        warningLogs = FileLogManager.getWarningLogs(context)
    }

    LaunchedEffect(Unit) {
        refreshLogs()
    }

    val filteredLogs = remember(crashLogs, warningLogs, logFilter) {
        when (logFilter) {
            "CRASH" -> crashLogs
            "WARNING" -> warningLogs
            else -> (crashLogs + warningLogs).sortedByDescending { it.file.lastModified() }
        }
    }

    Scaffold(
        topBar = {
            SettingsTopBar(onNavigateBack = onNavigateBack)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Section 1: Graphics settings
            item {
                Text(
                    "Rendimiento y Gráficos",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                GraphicsSettingsCard(
                    isLowGraphics = isLowGraphics,
                    onCheckedChange = {
                        isLowGraphics = it
                        viewModel.updateLowGraphicsQuality(it)
                    }
                )
            }

            // Section 1.2: System Overlays & Triggers
            item {
                Text(
                    "Superposiciones y Servicios del Sistema",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                SystemOverlaySettingsCard(viewModel = viewModel)
            }

            // Section 1.5: Third Party Licenses & Attributions
            item {
                Text(
                    "Atribuciones y Licencias",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                ThirdPartyLicensesCard()
            }

            // Section 2: Log Console (Warnings & Crashes)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Consola de Diagnóstico (Logs)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    if (filteredLogs.isNotEmpty()) {
                        TextButton(
                            onClick = {
                                FileLogManager.clearAllLogs(context)
                                refreshLogs()
                            },
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                            Icon(Icons.Default.DeleteSweep, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Limpiar todo", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Info Card explaining what these logs are for and how they help the creator
                DiagnosticHelpCard()

                // Filter chips
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf(
                        "ALL" to "Todos (${crashLogs.size + warningLogs.size})",
                        "CRASH" to "Crashes (${crashLogs.size})",
                        "WARNING" to "Warnings (${warningLogs.size})"
                    ).forEach { (filter, label) ->
                        val isSelected = logFilter == filter
                        FilterChip(
                            selected = isSelected,
                            onClick = { logFilter = filter },
                            label = { Text(label, fontSize = 11.sp) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            if (filteredLogs.isEmpty()) {
                item {
                    EmptyLogCard()
                }
            } else {
                items(filteredLogs) { log ->
                    LogItemRow(log = log, onClick = { selectedLog = log })
                }
            }
        }
    }

    // Detail Dialog Overlay for viewing logs
    selectedLog?.let { log ->
        LogDetailDialog(
            log = log,
            context = context,
            onDismiss = { selectedLog = null }
        )
    }
}

package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.screens.apppicker.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun AppPickerDialog(
    onDismiss: () -> Unit,
    onAppSelected: (packageName: String, appName: String) -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val pm = context.packageManager

    var installedApps by remember { mutableStateOf<List<AppInfo>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val apps = withContext(Dispatchers.IO) {
            try {
                val intent = android.content.Intent(android.content.Intent.ACTION_MAIN, null).apply {
                    addCategory(android.content.Intent.CATEGORY_LAUNCHER)
                }
                pm.queryIntentActivities(intent, 0)
                    .distinctBy { it.activityInfo.packageName }
                    .map { resolveInfo ->
                        val pkgName = resolveInfo.activityInfo.packageName
                        val label = resolveInfo.loadLabel(pm).toString()
                        val icon = try {
                            resolveInfo.loadIcon(pm)
                        } catch (e: Exception) {
                            null
                        }
                        AppInfo(pkgName, label, icon)
                    }.sortedBy { it.appName }
            } catch (e: Exception) {
                emptyList()
            }
        }
        installedApps = apps
        isLoading = false
    }

    var searchQuery by remember { mutableStateOf("") }
    val filteredApps = remember(searchQuery, installedApps) {
        if (searchQuery.isBlank()) {
            installedApps
        } else {
            installedApps.filter {
                it.appName.contains(searchQuery, ignoreCase = true) ||
                it.packageName.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Seleccionar Aplicación", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                AppPickerSearchField(
                    searchQuery = searchQuery,
                    onQueryChange = { searchQuery = it }
                )

                if (isLoading) {
                    AppPickerLoadingView(modifier = Modifier.weight(1f))
                } else if (filteredApps.isEmpty()) {
                    AppPickerEmptyState(modifier = Modifier.weight(1f))
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredApps.size) { index ->
                            val app = filteredApps[index]
                            AppPickerRow(
                                app = app,
                                onClick = { onAppSelected(app.packageName, app.appName) }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

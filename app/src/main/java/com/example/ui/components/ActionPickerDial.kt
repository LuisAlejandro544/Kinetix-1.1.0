package com.example.ui.components

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ActionType
import com.example.ui.screens.actionpicker.getCategoryForAction
import com.example.ui.screens.actionpicker.requiresAccessibility
import com.example.ui.screens.actionpicker.AccessibilityLockAlert

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionPickerDial(
    onDismiss: () -> Unit,
    onActionSelected: (ActionType) -> Unit
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Todos") }
    var showAccessibilityLockAlert by remember { mutableStateOf<ActionType?>(null) }

    val isAccessibilityActive = com.example.executor.ShortcutAccessibilityService.isServiceRunning()

    val categories = listOf("Todos", "Voz y Sonido", "Sistema", "Archivos y Datos", "Lógica y Código")

    val filteredActions = remember(searchQuery, selectedCategory) {
        ActionType.values().filter { type ->
            val matchesCategory = selectedCategory == "Todos" || getCategoryForAction(type) == selectedCategory
            val matchesSearch = type.displayName.contains(searchQuery, ignoreCase = true) ||
                    type.description.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    "Elige una Acción",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    "Agrega automatizaciones a tu flujo",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Search Input Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Buscar acción por nombre...", fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Limpiar", modifier = Modifier.size(16.dp))
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("action_search_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )

                // Category Tabs Scrollable Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    categories.forEach { category ->
                        val isSelected = category == selectedCategory
                        Surface(
                            onClick = { selectedCategory = category },
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            shape = RoundedCornerShape(16.dp),
                            border = if (isSelected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                            modifier = Modifier.testTag("category_chip_$category")
                        ) {
                            Text(
                                text = category,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                // LazyColumn list of filtered actions
                if (filteredActions.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.SearchOff,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "No se encontraron acciones",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.outline,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .testTag("action_picker_list"),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredActions) { type ->
                            val isLocked = requiresAccessibility(type) && !isAccessibilityActive

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isLocked) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f)
                                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                                    )
                                    .clickable {
                                        if (isLocked) {
                                            showAccessibilityLockAlert = type
                                        } else {
                                            onActionSelected(type)
                                        }
                                    }
                                    .border(
                                        width = 1.dp,
                                        color = if (isLocked) MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f) else Color.Transparent,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(12.dp)
                                    .testTag("action_picker_item_${type.name}"),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Action Icon or Lock badge
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isLocked) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
                                            else MaterialTheme.colorScheme.primaryContainer
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (isLocked) Icons.Default.Lock else type.icon,
                                        contentDescription = null,
                                        tint = if (isLocked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            type.displayName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = if (isLocked) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface
                                        )
                                        if (isLocked) {
                                            Surface(
                                                color = MaterialTheme.colorScheme.errorContainer,
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Text(
                                                    "BLOQUEADO",
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        if (isLocked) "⚠️ Requiere activar el Servicio de Accesibilidad de Kinetix." else type.description,
                                        fontSize = 11.sp,
                                        color = if (isLocked) MaterialTheme.colorScheme.error.copy(alpha = 0.7f) else MaterialTheme.colorScheme.outline,
                                        lineHeight = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        shape = RoundedCornerShape(24.dp)
    )

    // Alert Dialog overlay when selecting a locked action
    showAccessibilityLockAlert?.let { type ->
        AccessibilityLockAlert(
            type = type,
            context = context,
            onDismiss = { showAccessibilityLockAlert = null }
        )
    }
}

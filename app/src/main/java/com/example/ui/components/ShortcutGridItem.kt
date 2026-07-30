package com.example.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.Shortcut
import com.example.ui.screens.list.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShortcutGridItem(
    shortcut: Shortcut,
    isLowGraphicsQuality: Boolean,
    onRun: () -> Unit,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val baseColor = try {
        Color(android.graphics.Color.parseColor(shortcut.colorHex))
    } catch (e: Exception) {
        MaterialTheme.colorScheme.primary
    }

    var showDeleteDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(baseColor)
            .combinedClickable(
                onClick = onClick,
                onLongClick = { showDeleteDialog = true }
            )
    ) {
        // Base color gradient background (fallback & underlying canvas)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (isLowGraphicsQuality) {
                        androidx.compose.ui.graphics.SolidColor(baseColor)
                    } else {
                        Brush.verticalGradient(
                            colors = listOf(
                                baseColor,
                                baseColor.copy(alpha = 0.75f)
                            )
                        )
                    }
                )
        )

        // If custom photo is set, load it on top
        if (shortcut.customPhotoUri != null && !isLowGraphicsQuality) {
            AsyncImage(
                model = shortcut.customPhotoUri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Dark overlay for readable contrast over custom photo
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.45f))
            )
        }

        // Inner Padding content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // Translucent Circle Icon background for premium feel
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
                    .align(Alignment.TopStart),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getIconByName(shortcut.iconName),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Quick run button
            IconButton(
                onClick = onRun,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f))
                    .align(Alignment.TopEnd)
                    .testTag("run_shortcut_button_${shortcut.name.replace(" ", "_")}")
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "Ejecutar Atajo",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Labels
            Column(
                modifier = Modifier.align(Alignment.BottomStart)
            ) {
                Text(
                    text = shortcut.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White,
                    maxLines = 2,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = if (shortcut.actions.size == 1) "1 acción" else "${shortcut.actions.size} acciones",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium
                )
            }

            // Trigger indicator badges
            TriggerBadges(
                shortcut = shortcut,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }

    if (showDeleteDialog) {
        DeleteShortcutDialog(
            shortcut = shortcut,
            onConfirm = {
                onDelete()
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}

package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ActionData

@Composable
fun ActionListItem(
    index: Int,
    action: ActionData,
    totalCount: Int,
    onUpdateParams: (Map<String, String>) -> Unit,
    onDelete: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onSwap: (fromIndex: Int, toIndex: Int) -> Unit = { _, _ -> }
) {
    var offsetY by remember { mutableFloatStateOf(0f) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("action_item_$index"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Drag Handle
                Icon(
                    imageVector = Icons.Default.DragHandle,
                    contentDescription = "Arrastrar para reordenar paso",
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .size(24.dp)
                        .testTag("drag_handle_$index")
                        .pointerInput(index, totalCount) {
                            detectDragGestures(
                                onDragStart = { offsetY = 0f },
                                onDragEnd = { offsetY = 0f },
                                onDragCancel = { offsetY = 0f },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    offsetY += dragAmount.y
                                    if (offsetY < -60f && index > 0) {
                                        onSwap(index, index - 1)
                                        offsetY = 0f
                                    } else if (offsetY > 60f && index < totalCount - 1) {
                                        onSwap(index, index + 1)
                                        offsetY = 0f
                                    }
                                }
                            )
                        }
                )
                Spacer(modifier = Modifier.width(6.dp))

                // Number Circle Badge
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "${index + 1}",
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = action.type.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    action.type.displayName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.weight(1f))

                // Actions order handlers
                IconButton(
                    onClick = onMoveUp,
                    enabled = index > 0,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(Icons.Default.ArrowUpward, contentDescription = "Subir paso", modifier = Modifier.size(16.dp))
                }
                IconButton(
                    onClick = onMoveDown,
                    enabled = index < totalCount - 1,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(Icons.Default.ArrowDownward, contentDescription = "Bajar paso", modifier = Modifier.size(16.dp))
                }
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .size(28.dp)
                        .testTag("delete_action_button_$index")
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar paso", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                action.type.description,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.outline,
                lineHeight = 15.sp,
                modifier = Modifier.padding(start = 32.dp, bottom = 12.dp)
            )

            // Dynamic Inputs section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp)
            ) {
                DynamicActionInputs(
                    type = action.type,
                    params = action.params,
                    onParamsChanged = onUpdateParams
                )
            }
        }
    }
}

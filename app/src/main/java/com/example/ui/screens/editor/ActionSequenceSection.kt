package com.example.ui.screens.editor

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.ActionData
import com.example.ui.components.ActionListItem

@Composable
fun ActionSequenceSection(
    actionsList: List<ActionData>,
    onActionsListChange: (List<ActionData>) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Secuencia de Acciones",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                "${actionsList.size} ${if (actionsList.size == 1) "acción" else "acciones"}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }

        if (actionsList.isEmpty()) {
            EmptySequenceCard()
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                actionsList.forEachIndexed { index, action ->
                    ActionListItem(
                        index = index,
                        action = action,
                        totalCount = actionsList.size,
                        onUpdateParams = { updatedParams ->
                            val list = actionsList.toMutableList()
                            if (index in list.indices) {
                                list[index] = action.copy(params = updatedParams)
                                onActionsListChange(list)
                            }
                        },
                        onDelete = {
                            val list = actionsList.toMutableList()
                            if (index in list.indices) {
                                list.removeAt(index)
                                onActionsListChange(list)
                            }
                        },
                        onMoveUp = {
                            if (index > 0) {
                                val list = actionsList.toMutableList()
                                if (index in list.indices && (index - 1) in list.indices) {
                                    val temp = list[index]
                                    list[index] = list[index - 1]
                                    list[index - 1] = temp
                                    onActionsListChange(list)
                                }
                            }
                        },
                        onMoveDown = {
                            if (index < actionsList.size - 1) {
                                val list = actionsList.toMutableList()
                                if (index in list.indices && (index + 1) in list.indices) {
                                    val temp = list[index]
                                    list[index] = list[index + 1]
                                    list[index + 1] = temp
                                    onActionsListChange(list)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

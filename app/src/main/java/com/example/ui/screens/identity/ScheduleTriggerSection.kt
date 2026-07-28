package com.example.ui.screens.identity

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleTriggerSection(
    isScheduleTriggerEnabled: Boolean,
    onScheduleTriggerEnabledChange: (Boolean) -> Unit,
    triggerScheduleTime: String,
    onScheduleTimeChange: (String) -> Unit,
    triggerScheduleDays: String,
    onScheduleDaysChange: (String) -> Unit
) {
    val allDays = listOf(
        "MON" to "L",
        "TUE" to "M",
        "WED" to "X",
        "THU" to "J",
        "FRI" to "V",
        "SAT" to "S",
        "SUN" to "D"
    )

    Column {
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f))
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Programación por Horario / Alarma", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(
                    "Ejecuta este atajo automáticamente a una hora y días específicos.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.outline,
                    lineHeight = 14.sp
                )
            }
            Switch(
                checked = isScheduleTriggerEnabled,
                onCheckedChange = onScheduleTriggerEnabledChange,
                modifier = Modifier.testTag("schedule_trigger_switch")
            )
        }

        if (isScheduleTriggerEnabled) {
            Spacer(modifier = Modifier.height(12.dp))

            // Time input field
            OutlinedTextField(
                value = triggerScheduleTime,
                onValueChange = onScheduleTimeChange,
                label = { Text("Hora programada (HH:mm - 24h)") },
                placeholder = { Text("08:30") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("schedule_time_input"),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text("Días de repetición", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))

            val currentSelectedDays = if (triggerScheduleDays == "DAILY" || triggerScheduleDays.isBlank()) {
                allDays.map { it.first }.toSet()
            } else {
                triggerScheduleDays.split(",").filter { it.isNotBlank() }.toSet()
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                allDays.forEach { (code, label) ->
                    val isSelected = currentSelectedDays.contains(code)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            val newSet = currentSelectedDays.toMutableSet()
                            if (isSelected) {
                                newSet.remove(code)
                            } else {
                                newSet.add(code)
                            }
                            val newDaysStr = if (newSet.size == 7 || newSet.isEmpty()) {
                                "DAILY"
                            } else {
                                newSet.joinToString(",")
                            }
                            onScheduleDaysChange(newDaysStr)
                        },
                        label = { Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("day_chip_$code"),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }
        }
    }
}

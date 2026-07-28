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
fun BatteryTriggerSection(
    isBatteryTriggerEnabled: Boolean,
    onBatteryTriggerEnabledChange: (Boolean) -> Unit,
    triggerBatteryLevel: Int,
    onBatteryLevelChange: (Int) -> Unit,
    triggerBatteryType: String,
    onBatteryTypeChange: (String) -> Unit
) {
    Column {
        HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f))
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Disparador por Batería", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(
                    "Activar este atajo automáticamente al llegar a un porcentaje de batería.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.outline,
                    lineHeight = 14.sp
                )
            }
            Switch(
                checked = isBatteryTriggerEnabled,
                onCheckedChange = onBatteryTriggerEnabledChange,
                modifier = Modifier.testTag("battery_trigger_switch")
            )
        }

        if (isBatteryTriggerEnabled) {
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "Porcentaje de batería: $triggerBatteryLevel%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Slider(
                        value = triggerBatteryLevel.toFloat(),
                        onValueChange = { onBatteryLevelChange(it.toInt()) },
                        valueRange = 0f..100f,
                        steps = 100,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("battery_level_slider")
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Condición de activación:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf(
                            "EQUALS" to "Igual",
                            "FALLS_BELOW" to "Menor",
                            "RISES_ABOVE" to "Mayor"
                        ).forEach { (type, label) ->
                            val isSelected = triggerBatteryType == type
                            FilterChip(
                                selected = isSelected,
                                onClick = { onBatteryTypeChange(type) },
                                label = { Text(label, fontSize = 11.sp) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("battery_chip_$type"),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

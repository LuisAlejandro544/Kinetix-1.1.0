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
fun HeadphonesTriggerSection(
    isHeadphonesTriggerEnabled: Boolean,
    onHeadphonesTriggerEnabledChange: (Boolean) -> Unit,
    triggerHeadphonesType: String,
    onHeadphonesTypeChange: (String) -> Unit
) {
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
                Text("Disparador por Auriculares", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(
                    "Activar este atajo cuando se conectan o desconectan auriculares.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.outline,
                    lineHeight = 14.sp
                )
            }
            Switch(
                checked = isHeadphonesTriggerEnabled,
                onCheckedChange = onHeadphonesTriggerEnabledChange,
                modifier = Modifier.testTag("headphones_trigger_switch")
            )
        }

        if (isHeadphonesTriggerEnabled) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf(
                    "CONNECTED" to "Conectados",
                    "DISCONNECTED" to "Desconectados"
                ).forEach { (type, label) ->
                    val isSelected = triggerHeadphonesType == type
                    FilterChip(
                        selected = isSelected,
                        onClick = { onHeadphonesTypeChange(type) },
                        label = { Text(label, fontSize = 11.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("headphones_chip_$type"),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }
        }
    }
}

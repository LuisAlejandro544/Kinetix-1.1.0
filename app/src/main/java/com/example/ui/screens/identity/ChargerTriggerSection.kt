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
fun ChargerTriggerSection(
    isChargerTriggerEnabled: Boolean,
    onChargerTriggerEnabledChange: (Boolean) -> Unit,
    triggerChargerType: String,
    onChargerTypeChange: (String) -> Unit
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
                Text("Disparador por Cargador", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(
                    "Activar este atajo cuando se conecta o desconecta de la corriente.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.outline,
                    lineHeight = 14.sp
                )
            }
            Switch(
                checked = isChargerTriggerEnabled,
                onCheckedChange = onChargerTriggerEnabledChange,
                modifier = Modifier.testTag("charger_trigger_switch")
            )
        }

        if (isChargerTriggerEnabled) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf(
                    "CONNECTED" to "Conectado",
                    "DISCONNECTED" to "Desconectado"
                ).forEach { (type, label) ->
                    val isSelected = triggerChargerType == type
                    FilterChip(
                        selected = isSelected,
                        onClick = { onChargerTypeChange(type) },
                        label = { Text(label, fontSize = 11.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("charger_chip_$type"),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }
        }
    }
}

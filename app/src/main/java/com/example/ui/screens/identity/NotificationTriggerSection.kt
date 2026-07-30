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

@Composable
fun NotificationTriggerSection(
    isNotificationTriggerEnabled: Boolean,
    onNotificationTriggerEnabledChange: (Boolean) -> Unit,
    triggerNotificationApp: String,
    onNotificationAppChange: (String) -> Unit,
    triggerNotificationKeyword: String,
    onNotificationKeywordChange: (String) -> Unit
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
                Text("Lector de Notificaciones Entrantes", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(
                    "Inicia este atajo cuando se reciba una notificación coincidente.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.outline,
                    lineHeight = 14.sp
                )
            }
            Switch(
                checked = isNotificationTriggerEnabled,
                onCheckedChange = onNotificationTriggerEnabledChange,
                modifier = Modifier.testTag("notification_trigger_switch")
            )
        }

        if (isNotificationTriggerEnabled) {
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = triggerNotificationApp,
                onValueChange = onNotificationAppChange,
                label = { Text("Filtrar por App (ej: com.whatsapp o vació para todas)") },
                placeholder = { Text("com.whatsapp") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("notification_app_input"),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = triggerNotificationKeyword,
                onValueChange = onNotificationKeywordChange,
                label = { Text("Palabra clave en notificación (ej: Código, Urgente)") },
                placeholder = { Text("Urgente") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("notification_keyword_input"),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

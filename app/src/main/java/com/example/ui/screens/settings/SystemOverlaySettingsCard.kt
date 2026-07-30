package com.example.ui.screens.settings

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.executor.AssistiveTouchService
import com.example.ui.ShortcutViewModel

@Composable
fun SystemOverlaySettingsCard(viewModel: ShortcutViewModel) {
    val context = LocalContext.current
    var isAssistiveTouch by remember { mutableStateOf(viewModel.prefsManager.isAssistiveTouchEnabled) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Assistive Touch Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.TouchApp,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Botón Flotante (Assistive Touch)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(
                            "Muestra un botón superpuesto en pantalla para ejecutar atajos rápidamente desde cualquier app.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.outline,
                            lineHeight = 14.sp
                        )
                    }
                }

                Switch(
                    checked = isAssistiveTouch,
                    onCheckedChange = { enabled ->
                        if (enabled) {
                            if (!Settings.canDrawOverlays(context)) {
                                Toast.makeText(
                                    context,
                                    "Concede el permiso de superposición en pantalla",
                                    Toast.LENGTH_LONG
                                ).show()
                                val intent = Intent(
                                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:${context.packageName}")
                                )
                                context.startActivity(intent)
                            } else {
                                isAssistiveTouch = true
                                viewModel.prefsManager.updateAssistiveTouchEnabled(true)
                                AssistiveTouchService.start(context)
                            }
                        } else {
                            isAssistiveTouch = false
                            viewModel.prefsManager.updateAssistiveTouchEnabled(false)
                            AssistiveTouchService.stop(context)
                        }
                    },
                    modifier = Modifier.testTag("assistive_touch_switch")
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f))
            Spacer(modifier = Modifier.height(16.dp))

            // Notification Reader Permission Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.NotificationsActive,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Acceso a Notificaciones Entrantes", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(
                            "Permite que Kinetix detecte notificaciones recibidas para activar atajos automáticos.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.outline,
                            lineHeight = 14.sp
                        )
                    }
                }

                OutlinedButton(
                    onClick = {
                        try {
                            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Abre Ajustes > Notificaciones > Acceso a notificaciones", Toast.LENGTH_LONG).show()
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("notification_access_button")
                ) {
                    Text("Configurar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

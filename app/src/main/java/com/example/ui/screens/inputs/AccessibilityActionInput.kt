package com.example.ui.screens.inputs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccessibilityActionInput(
    params: Map<String, String>,
    onParamsChanged: (Map<String, String>) -> Unit
) {
    val actionType = params["actionType"] ?: "Back"
    val options = listOf(
        "Back" to "Atrás",
        "Home" to "Inicio",
        "Notifications" to "Notificaciones",
        "Quick Settings" to "Ajustes Rápidos",
        "Power Dialog" to "Menú Encendido",
        "Lock Screen" to "Bloquear Pantalla"
    )
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            "Acción de accesibilidad global a ejecutar:",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(bottom = 8.dp)
        ) {
            items(options.size) { index ->
                val opt = options[index]
                val isSelected = actionType == opt.first
                FilterChip(
                    selected = isSelected,
                    onClick = { onParamsChanged(params + ("actionType" to opt.first)) },
                    label = { Text(opt.second, fontSize = 11.sp) }
                )
            }
        }
        Text(
            "Nota: Ejecutar estas acciones requiere activar el Servicio de Accesibilidad de la aplicación. Al ejecutar por primera vez, se te redirigirá a los Ajustes de Accesibilidad si no está activado.",
            style = MaterialTheme.typography.bodySmall,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.outline,
            lineHeight = 14.sp
        )
    }
}

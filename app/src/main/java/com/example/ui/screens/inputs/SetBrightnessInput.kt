package com.example.ui.screens.inputs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SetBrightnessInput(
    params: Map<String, String>,
    onParamsChanged: (Map<String, String>) -> Unit
) {
    val brightnessPercentStr = params["brightnessPercent"] ?: "50"
    val brightnessPercent = brightnessPercentStr.toFloatOrNull() ?: 50f
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Brillo de pantalla:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("${brightnessPercent.toInt()}%", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
        }
        Slider(
            value = brightnessPercent,
            onValueChange = { onParamsChanged(params + ("brightnessPercent" to it.toInt().toString())) },
            valueRange = 0f..100f,
            steps = 19,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            "Nota: Ajustar el brillo requiere permisos para escribir ajustes del sistema (WRITE_SETTINGS). Si no está concedido, la app te redirigirá a los ajustes para activarlo.",
            style = MaterialTheme.typography.bodySmall,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.outline,
            lineHeight = 14.sp
        )
    }
}

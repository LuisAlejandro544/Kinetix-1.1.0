package com.example.ui.screens.inputs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetVolumeInput(
    params: Map<String, String>,
    onParamsChanged: (Map<String, String>) -> Unit
) {
    val streamType = params["streamType"] ?: "Music"
    val volumePercentStr = params["volumePercent"] ?: "50"
    val volumePercent = volumePercentStr.toFloatOrNull() ?: 50f

    val streamTypes = listOf(
        "Music" to "Multimedia",
        "Ring" to "Timbre",
        "Notification" to "Notificaciones",
        "Alarm" to "Alarma"
    )

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Canal de Audio:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(streamTypes.size) { index ->
                val item = streamTypes[index]
                val isSelected = streamType == item.first
                FilterChip(
                    selected = isSelected,
                    onClick = { onParamsChanged(params + ("streamType" to item.first)) },
                    label = { Text(item.second) }
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Volumen deseado:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("${volumePercent.toInt()}%", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
        }

        Slider(
            value = volumePercent,
            onValueChange = { onParamsChanged(params + ("volumePercent" to it.toInt().toString())) },
            valueRange = 0f..100f,
            steps = 19,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

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
fun SystemInfoInput(
    params: Map<String, String>,
    onParamsChanged: (Map<String, String>) -> Unit
) {
    val infoType = params["infoType"] ?: "Battery Level"
    val options = listOf(
        "Battery Level" to "Batería (%)",
        "Device Model" to "Modelo",
        "Android Version" to "Versión Android"
    )
    Column {
        Text("Dato del sistema a obtener:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(4.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(options.size) { index ->
                val opt = options[index]
                val isSelected = infoType == opt.first
                FilterChip(
                    selected = isSelected,
                    onClick = { onParamsChanged(params + ("infoType" to opt.first)) },
                    label = { Text(opt.second, fontSize = 11.sp) }
                )
            }
        }
    }
}

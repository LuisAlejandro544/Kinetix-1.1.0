package com.example.ui.screens.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackgroundCameraCaptureInput(
    params: Map<String, String>,
    onParamsChanged: (Map<String, String>) -> Unit
) {
    val cameraType = params["cameraType"] ?: "BACK"
    val saveDestination = params["saveDestination"] ?: "GALLERY"
    val options = listOf(
        "BACK" to "Cámara Trasera",
        "FRONT" to "Cámara Frontal"
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            "Cámara a utilizar:",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(options.size) { index ->
                val opt = options[index]
                val isSelected = cameraType == opt.first
                FilterChip(
                    selected = isSelected,
                    onClick = { onParamsChanged(params + ("cameraType" to opt.first)) },
                    label = { Text(opt.second, fontSize = 11.sp) }
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            "Destino de guardado:",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                val isGallerySelected = saveDestination == "GALLERY"
                FilterChip(
                    selected = isGallerySelected,
                    onClick = { onParamsChanged(params + ("saveDestination" to "GALLERY")) },
                    label = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Photo,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Text("Galería pública", fontSize = 11.sp)
                        }
                    }
                )
            }

            item {
                FilterChip(
                    selected = false,
                    enabled = false,
                    onClick = { },
                    label = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Text("Apartado especial", fontSize = 11.sp)
                            Box(
                                modifier = Modifier
                                    .padding(start = 2.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "🔒 Próximamente",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                )
            }
        }

        Text(
            "Nota: Requiere permisos de Cámara. La foto se capturará silenciosamente en segundo plano sin mostrar ninguna interfaz de usuario y se guardará en el destino elegido.",
            style = MaterialTheme.typography.bodySmall,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.outline,
            lineHeight = 14.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimulateGesturesInput(
    params: Map<String, String>,
    onParamsChanged: (Map<String, String>) -> Unit
) {
    val gestureType = params["gestureType"] ?: "TAP"
    val x1 = params["x1"] ?: "500"
    val y1 = params["y1"] ?: "1000"
    val x2 = params["x2"] ?: "500"
    val y2 = params["y2"] ?: "500"
    val duration = params["duration"] ?: "300"

    val options = listOf(
        "TAP" to "Simular Toque",
        "SWIPE" to "Simular Desplazamiento"
    )

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            "Tipo de gesto a simular:",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(options.size) { index ->
                val opt = options[index]
                val isSelected = gestureType == opt.first
                FilterChip(
                    selected = isSelected,
                    onClick = { onParamsChanged(params + ("gestureType" to opt.first)) },
                    label = { Text(opt.second, fontSize = 11.sp) }
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = x1,
                onValueChange = { onParamsChanged(params + ("x1" to it)) },
                label = { Text("X (Inicio)") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = y1,
                onValueChange = { onParamsChanged(params + ("y1" to it)) },
                label = { Text("Y (Inicio)") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        if (gestureType == "SWIPE") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = x2,
                    onValueChange = { onParamsChanged(params + ("x2" to it)) },
                    label = { Text("X (Fin)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = y2,
                    onValueChange = { onParamsChanged(params + ("y2" to it)) },
                    label = { Text("Y (Fin)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }

        OutlinedTextField(
            value = duration,
            onValueChange = { onParamsChanged(params + ("duration" to it)) },
            label = { Text("Duración (milisegundos)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Text(
            "Nota: Requiere tener activo el servicio de accesibilidad de Kinetix. Utiliza las coordenadas de píxeles reales de tu pantalla.",
            style = MaterialTheme.typography.bodySmall,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.outline,
            lineHeight = 14.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClipboardSilentInput(
    params: Map<String, String>,
    onParamsChanged: (Map<String, String>) -> Unit
) {
    val operation = params["operation"] ?: "WRITE"
    val text = params["text"] ?: "{resultado}"

    val options = listOf(
        "WRITE" to "Copiar al portapapeles",
        "READ" to "Leer del portapapeles"
    )

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            "Operación del Portapapeles:",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(options.size) { index ->
                val opt = options[index]
                val isSelected = operation == opt.first
                FilterChip(
                    selected = isSelected,
                    onClick = { onParamsChanged(params + ("operation" to opt.first)) },
                    label = { Text(opt.second, fontSize = 11.sp) }
                )
            }
        }

        if (operation == "WRITE") {
            OutlinedTextField(
                value = text,
                onValueChange = { onParamsChanged(params + ("text" to it)) },
                label = { Text("Texto a copiar") },
                modifier = Modifier.fillMaxWidth(),
                supportingText = { Text("Puedes usar {resultado} para copiar el output del paso anterior.") }
            )
        } else {
            Text(
                "La operación de lectura extraerá el texto actual del portapapeles y lo pasará como entrada al siguiente paso de la automatización.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 15.sp
            )
        }
    }
}

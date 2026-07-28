package com.example.ui.screens.inputs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SpeakTextInput(
    params: Map<String, String>,
    onParamsChanged: (Map<String, String>) -> Unit
) {
    val textValue = params["text"] ?: ""
    val rateStr = params["speechRate"] ?: "1.0"
    val rate = rateStr.toFloatOrNull() ?: 1.0f
    
    val selectedEngine = params["engine"] ?: ""
    val selectedVoice = params["voice"] ?: ""

    val context = androidx.compose.ui.platform.LocalContext.current
    val ttsManager = remember { com.example.executor.TtsManager.getInstance(context) }
    
    var engines by remember { mutableStateOf(emptyList<android.speech.tts.TextToSpeech.EngineInfo>()) }
    var voices by remember { mutableStateOf(emptyList<android.speech.tts.Voice>()) }
    
    // Periodically fetch initialized list
    LaunchedEffect(ttsManager.isInitializedFlow) {
        ttsManager.isInitializedFlow.collect { initialized ->
            if (initialized) {
                engines = ttsManager.getAvailableEngines()
                voices = ttsManager.getAvailableVoices()
            }
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        CleanTextFieldWithLink(
            value = textValue,
            onValueChange = { onParamsChanged(params + ("text" to it)) },
            label = "Texto a pronunciar",
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            maxLines = 2
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Velocidad de lectura:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("${(Math.round(rate * 10f) / 10f)}x", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
        }

        Slider(
            value = rate,
            onValueChange = { 
                val rounded = (Math.round(it * 10f) / 10f)
                onParamsChanged(params + ("speechRate" to rounded.toString())) 
            },
            valueRange = 0.5f..2.5f,
            steps = 19,
            modifier = Modifier.fillMaxWidth()
        )

        // Local engine and voice selection
        if (engines.isNotEmpty()) {
            var engineExpanded by remember { mutableStateOf(false) }
            Text("Motor de voz (TTS Engine):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Box {
                OutlinedButton(
                    onClick = { engineExpanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = engines.find { it.name == selectedEngine }?.label ?: selectedEngine.ifEmpty { "Motor por defecto" },
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.size(16.dp))
                }
                DropdownMenu(
                    expanded = engineExpanded,
                    onDismissRequest = { engineExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Motor por defecto") },
                        onClick = {
                            onParamsChanged(params + ("engine" to "") + ("voice" to ""))
                            engineExpanded = false
                        }
                    )
                    engines.forEach { engineInfo ->
                        DropdownMenuItem(
                            text = { Text(engineInfo.label.ifEmpty { engineInfo.name }) },
                            onClick = {
                                onParamsChanged(params + ("engine" to engineInfo.name) + ("voice" to ""))
                                engineExpanded = false
                            }
                        )
                    }
                }
            }
        }

        if (voices.isNotEmpty()) {
            var voiceExpanded by remember { mutableStateOf(false) }
            Text("Seleccionar Voz:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Box {
                OutlinedButton(
                    onClick = { voiceExpanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val displayName = selectedVoice.substringAfter("es-es-x-").substringAfter("es-ES-").ifEmpty { "Voz por defecto" }
                    Text(
                        text = displayName,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.size(16.dp))
                }
                DropdownMenu(
                    expanded = voiceExpanded,
                    onDismissRequest = { voiceExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Voz por defecto") },
                        onClick = {
                            onParamsChanged(params + ("voice" to ""))
                            voiceExpanded = false
                        }
                    )
                    voices.forEach { voice ->
                        val displayName = voice.name.substringAfter("es-es-x-").substringAfter("es-ES-")
                        DropdownMenuItem(
                            text = { Text(displayName) },
                            onClick = {
                                onParamsChanged(params + ("voice" to voice.name))
                                voiceExpanded = false
                            }
                        )
                    }
                }
            }
        }

        // Preview button
        Button(
            onClick = {
                ttsManager.speakPreview(
                    text = textValue,
                    rate = rate,
                    enginePackage = selectedEngine.ifEmpty { null },
                    voiceName = selectedVoice.ifEmpty { null }
                )
            },
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Escuchar preview 🗣️", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ShowNotificationInput(
    params: Map<String, String>,
    onParamsChanged: (Map<String, String>) -> Unit
) {
    val title = params["title"] ?: ""
    val message = params["message"] ?: ""
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = title,
            onValueChange = { onParamsChanged(params + ("title" to it)) },
            label = { Text("Título de Notificación") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        CleanTextFieldWithLink(
            value = message,
            onValueChange = { onParamsChanged(params + ("message" to it)) },
            label = "Mensaje",
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            maxLines = 2
        )
    }
}

@Composable
fun VibrateInput(
    params: Map<String, String>,
    onParamsChanged: (Map<String, String>) -> Unit
) {
    val duration = params["duration"] ?: "500"
    OutlinedTextField(
        value = duration,
        onValueChange = { onParamsChanged(params + ("duration" to it)) },
        label = { Text("Duración de vibración (ms)") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun OpenUrlInput(
    params: Map<String, String>,
    onParamsChanged: (Map<String, String>) -> Unit
) {
    val url = params["url"] ?: ""
    CleanTextFieldWithLink(
        value = url,
        onValueChange = { onParamsChanged(params + ("url" to it)) },
        label = "URL / Enlace Web",
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
    )
}

@Composable
fun AlertDialogInput(
    params: Map<String, String>,
    onParamsChanged: (Map<String, String>) -> Unit
) {
    val title = params["title"] ?: ""
    val message = params["message"] ?: ""
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = title,
            onValueChange = { onParamsChanged(params + ("title" to it)) },
            label = { Text("Título de Alerta") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        CleanTextFieldWithLink(
            value = message,
            onValueChange = { onParamsChanged(params + ("message" to it)) },
            label = "Mensaje Alerta",
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            maxLines = 2
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaySoundInput(
    params: Map<String, String>,
    onParamsChanged: (Map<String, String>) -> Unit
) {
    val soundType = params["soundType"] ?: "Beep"
    val options = listOf(
        "Beep" to "Pitido simple 🔔",
        "Alert" to "Alerta 🚨"
    )
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            "Tipo de sonido a reproducir:",
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
                val isSelected = soundType == opt.first
                FilterChip(
                    selected = isSelected,
                    onClick = { onParamsChanged(params + ("soundType" to opt.first)) },
                    label = { Text(opt.second, fontSize = 11.sp) }
                )
            }
        }
        Text(
            "Reproduce una señal acústica interactiva similar al sistema operativo iOS.",
            style = MaterialTheme.typography.bodySmall,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.outline,
            lineHeight = 14.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        val context = androidx.compose.ui.platform.LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        Button(
            onClick = {
                coroutineScope.launch {
                    com.example.executor.strategies.PlaySoundStrategy.playSound(context, soundType)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Escuchar preview 🔊", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ShareTextInput(
    params: Map<String, String>,
    onParamsChanged: (Map<String, String>) -> Unit
) {
    val textToShare = params["text"] ?: ""
    CleanTextFieldWithLink(
        value = textToShare,
        onValueChange = { onParamsChanged(params + ("text" to it)) },
        label = "Texto a compartir",
        modifier = Modifier.fillMaxWidth(),
        singleLine = false,
        maxLines = 2
    )
}

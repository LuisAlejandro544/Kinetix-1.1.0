package com.example.ui.screens.inputs

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.executor.TtsManager

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

    val context = LocalContext.current
    val ttsManager = remember { TtsManager.getInstance(context) }
    
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

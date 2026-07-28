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
fun ExecJavascriptInput(
    params: Map<String, String>,
    onParamsChanged: (Map<String, String>) -> Unit
) {
    val code = params["code"] ?: ""
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = code,
            onValueChange = { onParamsChanged(params + ("code" to it)) },
            label = { Text("Código JavaScript (QuickJS)") },
            modifier = Modifier.fillMaxWidth().height(150.dp),
            singleLine = false,
            maxLines = 10,
            supportingText = { Text("La última línea o expresión evaluada es el resultado.") }
        )
        Text(
            "Ejecuta código JavaScript estándar de manera segura. Puedes usar variables reemplazables como {resultado} o escribir lógica JS de forma nativa.",
            style = MaterialTheme.typography.bodySmall,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.outline,
            lineHeight = 14.sp
        )
    }
}

@Composable
fun TermuxCommandInput(
    params: Map<String, String>,
    onParamsChanged: (Map<String, String>) -> Unit
) {
    val command = params["command"] ?: ""
    val args = params["args"] ?: ""
    val runInBackgroundStr = params["runInBackground"] ?: "true"
    val runInBackground = runInBackgroundStr.toBoolean()

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = command,
            onValueChange = { onParamsChanged(params + ("command" to it)) },
            label = { Text("Comando / Script de Termux") },
            placeholder = { Text("ej: echo 'Hola Mundo!'") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = args,
            onValueChange = { onParamsChanged(params + ("args" to it)) },
            label = { Text("Argumentos adicionales") },
            placeholder = { Text("ej: --verbose") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1.0f)) {
                Text(
                    "Ejecutar en segundo plano",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Evita abrir la ventana de terminal de Termux.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Switch(
                checked = runInBackground,
                onCheckedChange = { onParamsChanged(params + ("runInBackground" to it.toString())) }
            )
        }
    }
}

@Composable
fun CustomCodeInput(
    params: Map<String, String>,
    onParamsChanged: (Map<String, String>) -> Unit
) {
    val script = params["script"] ?: ""
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = script,
            onValueChange = { onParamsChanged(params + ("script" to it)) },
            label = { Text("Script Personalizado de Atajo") },
            modifier = Modifier.fillMaxWidth().height(150.dp),
            singleLine = false,
            maxLines = 10,
            supportingText = { Text("Escribe sentencias como SET, PRINT, RETURN, TTS.") }
        )
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            )
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text("Sintaxis Soportada:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "• PRINT \"mi mensaje\" + {resultado}\n" +
                    "• SET variable = {resultado} + \" sufijo\"\n" +
                    "• TTS \"Pronunciar texto de variable \" + variable\n" +
                    "• UPPERCASE {resultado}\n" +
                    "• RETURN variable",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    fontSize = 10.sp,
                    lineHeight = 14.sp
                )
            }
        }
    }
}

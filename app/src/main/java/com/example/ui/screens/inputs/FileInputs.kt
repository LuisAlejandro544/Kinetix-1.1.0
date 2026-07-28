package com.example.ui.screens.inputs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WriteFileInput(
    params: Map<String, String>,
    onParamsChanged: (Map<String, String>) -> Unit
) {
    val fileName = params["fileName"] ?: "atajo_archivo.txt"
    val content = params["content"] ?: "{resultado}"
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = fileName,
            onValueChange = { onParamsChanged(params + ("fileName" to it)) },
            label = { Text("Nombre del archivo") },
            supportingText = { Text("Ej: notas.txt o registro.log") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        CleanTextFieldWithLink(
            value = content,
            onValueChange = { onParamsChanged(params + ("content" to it)) },
            label = "Contenido a escribir",
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            maxLines = 4
        )
    }
}

@Composable
fun ReadFileInput(
    params: Map<String, String>,
    onParamsChanged: (Map<String, String>) -> Unit
) {
    val fileName = params["fileName"] ?: "atajo_archivo.txt"
    OutlinedTextField(
        value = fileName,
        onValueChange = { onParamsChanged(params + ("fileName" to it)) },
        label = { Text("Nombre del archivo a leer") },
        supportingText = { Text("El contenido leído se pasará como salida del paso") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Composable
fun AppendFileInput(
    params: Map<String, String>,
    onParamsChanged: (Map<String, String>) -> Unit
) {
    val fileName = params["fileName"] ?: "atajo_archivo.txt"
    val content = params["content"] ?: "{resultado}"
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = fileName,
            onValueChange = { onParamsChanged(params + ("fileName" to it)) },
            label = { Text("Nombre del archivo") },
            supportingText = { Text("Ej: registro_actividades.txt") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        CleanTextFieldWithLink(
            value = content,
            onValueChange = { onParamsChanged(params + ("content" to it)) },
            label = "Contenido a añadir (nueva línea)",
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            maxLines = 4
        )
    }
}

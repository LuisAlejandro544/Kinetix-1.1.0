package com.example.ui.screens.inputs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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

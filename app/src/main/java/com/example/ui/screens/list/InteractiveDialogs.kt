package com.example.ui.screens.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ui.ShortcutViewModel

@Composable
fun InteractiveDialogs(viewModel: ShortcutViewModel) {
    // Interactive Dialog Overlays (Suspended Execution States)
    viewModel.showAlertDialogState?.let { dialog ->
        AlertDialog(
            onDismissRequest = { dialog.onDismiss() },
            title = { Text(dialog.title) },
            text = { Text(dialog.message) },
            confirmButton = {
                Button(
                    onClick = { dialog.onDismiss() },
                    modifier = Modifier.testTag("dialog_ok_button")
                ) {
                    Text("Aceptar")
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }

    viewModel.showPromptDialogState?.let { promptState ->
        var promptValue by remember { mutableStateOf(promptState.defaultValue) }

        AlertDialog(
            onDismissRequest = { /* Force response to resume execution flow */ },
            title = { Text("Atajo requiere Entrada") },
            text = {
                Column {
                    Text(promptState.prompt, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(bottom = 8.dp))
                    OutlinedTextField(
                        value = promptValue,
                        onValueChange = { promptValue = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("dialog_prompt_input"),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { promptState.onSubmit(promptValue) },
                    modifier = Modifier.testTag("dialog_prompt_submit")
                ) {
                    Text("Enviar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { promptState.onSubmit("") } // send empty to avoid deadlock
                ) {
                    Text("Omitir")
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}

package com.example.ui.screens.list

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.example.data.Shortcut

@Composable
fun DeleteShortcutDialog(
    shortcut: Shortcut,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Eliminar Atajo") },
        text = { Text("¿Estás seguro de que quieres eliminar el atajo \"${shortcut.name}\"?") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

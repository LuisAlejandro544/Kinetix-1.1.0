package com.example.ui

data class AlertDialogState(
    val title: String,
    val message: String,
    val onDismiss: () -> Unit
)

data class PromptDialogState(
    val prompt: String,
    val defaultValue: String,
    val onSubmit: (String) -> Unit
)

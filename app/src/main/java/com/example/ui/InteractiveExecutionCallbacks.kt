package com.example.ui

import com.example.executor.ShortcutExecutionCallbacks

class InteractiveExecutionCallbacks(
    private val onShowAlertDialog: suspend (title: String, message: String) -> Unit,
    private val onPromptInputReceived: suspend (prompt: String, defaultValue: String) -> String,
    private val onLogReceived: (message: String) -> Unit,
    private val onProgressUpdated: (step: Int, totalSteps: Int, name: String) -> Unit
) : ShortcutExecutionCallbacks {

    override suspend fun onShowDialog(title: String, message: String) {
        onShowAlertDialog(title, message)
    }

    override suspend fun onPromptInput(prompt: String, defaultValue: String): String {
        return onPromptInputReceived(prompt, defaultValue)
    }

    override fun onLog(message: String) {
        onLogReceived(message)
    }

    override fun onProgress(currentStep: Int, totalSteps: Int, actionType: String) {
        onProgressUpdated(currentStep, totalSteps, actionType)
    }
}

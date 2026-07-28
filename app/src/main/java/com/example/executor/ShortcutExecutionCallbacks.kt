package com.example.executor

interface ShortcutExecutionCallbacks {
    suspend fun onShowDialog(title: String, message: String)
    suspend fun onPromptInput(prompt: String, defaultValue: String): String
    fun onLog(message: String)
    fun onProgress(currentStep: Int, totalSteps: Int, actionType: String)
}

package com.example.executor

import android.content.Context

class BackgroundCallbacks(private val context: Context) : ShortcutExecutionCallbacks {
    override suspend fun onShowDialog(title: String, message: String) {
        FileLogManager.logWarning(
            context,
            "BackgroundExecutor",
            "Diálogo ignorado en segundo plano: [$title] $message"
        )
    }

    override suspend fun onPromptInput(prompt: String, defaultValue: String): String {
        FileLogManager.logWarning(
            context,
            "BackgroundExecutor",
            "Pregunta ignorada en segundo plano: $prompt. Usando valor por defecto: $defaultValue"
        )
        return defaultValue
    }

    override fun onLog(message: String) {
        android.util.Log.d("BackgroundExecutor", message)
    }

    override fun onProgress(currentStep: Int, totalSteps: Int, actionType: String) {
        android.util.Log.d("BackgroundExecutor", "Paso $currentStep/$totalSteps: $actionType")
    }
}

package com.example.ui

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.data.Shortcut
import com.example.executor.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.Toast

class ShortcutExecutorEngine(
    private val context: Context,
    private val ttsManager: TtsManager,
    private val scope: CoroutineScope,
    private val onLog: (String) -> Unit
) {
    var isExecuting by mutableStateOf(false)
        private set
    var currentStep by mutableStateOf(0)
        private set
    var totalSteps by mutableStateOf(0)
        private set
    var activeStepName by mutableStateOf("")
        private set
    var executionLogs by mutableStateOf<List<String>>(emptyList())
        private set
    var showConsole by mutableStateOf(false)

    var showAlertDialogState by mutableStateOf<AlertDialogState?>(null)
        private set
    var showPromptDialogState by mutableStateOf<PromptDialogState?>(null)
        private set

    fun runShortcut(shortcut: Shortcut) {
        if (isExecuting) return
        isExecuting = true
        currentStep = 0
        totalSteps = shortcut.actions.size
        activeStepName = ""
        executionLogs = emptyList()
        showConsole = false

        addLog("🚀 Iniciando atajo: \"${shortcut.name}\"")

        scope.launch {
            try {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Ejecutando: \"${shortcut.name}\"", Toast.LENGTH_SHORT).show()
                }
                val callbacks = InteractiveExecutionCallbacks(
                    onShowAlertDialog = { title, message ->
                        val deferred = CompletableDeferred<Unit>()
                        showAlertDialogState = AlertDialogState(title, message) {
                            showAlertDialogState = null
                            deferred.complete(Unit)
                        }
                        deferred.await()
                    },
                    onPromptInputReceived = { prompt, defaultValue ->
                        val deferred = CompletableDeferred<String>()
                        showPromptDialogState = PromptDialogState(prompt, defaultValue) { input ->
                            showPromptDialogState = null
                            deferred.complete(input)
                        }
                        deferred.await()
                    },
                    onLogReceived = { message ->
                        addLog(message)
                    },
                    onProgressUpdated = { step, total, name ->
                        currentStep = step
                        totalSteps = total
                        activeStepName = name
                    }
                )

                val executor = ShortcutExecutor(
                    context = context,
                    tts = ttsManager.getTtsEngine(),
                    callbacks = callbacks
                )

                val finalOutput = executor.execute(shortcut.actions)
                addLog("🏁 Salida final del atajo: \"$finalOutput\"")
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "¡Atajo completado con éxito!", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                addLog("🛑 Atajo detenido debido a un error: ${e.localizedMessage}")
                FileLogManager.logWarning(context, "ShortcutExecution", "Atajo detenido con error en \"${shortcut.name}\": ${e.localizedMessage}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Atajo detenido con error.", Toast.LENGTH_SHORT).show()
                }
            } finally {
                isExecuting = false
            }
        }
    }

    fun addLog(message: String) {
        onLog(message)
        scope.launch(Dispatchers.Main) {
            executionLogs = executionLogs + message
        }
    }

    fun clearLogs() {
        executionLogs = emptyList()
        showConsole = false
    }
}

package com.example.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.example.executor.TtsManager
import com.example.executor.SystemTriggerManager
import com.example.executor.FileLogManager
import com.example.ui.preferences.KinetixPreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShortcutViewModel @JvmOverloads constructor(
    application: Application,
    private val repository: ShortcutRepository = ShortcutRepository(ShortcutDatabase.getDatabase(application).shortcutDao()),
    private val ttsManager: TtsManager = TtsManager.getInstance(application),
    private val preferencesManager: KinetixPreferencesManager = KinetixPreferencesManager(application)
) : AndroidViewModel(application) {

    val allShortcuts: StateFlow<List<Shortcut>> = repository.allShortcuts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val automationHandler = ShortcutAutomationHandler(
        allShortcutsProvider = { allShortcuts.value },
        runShortcut = { runShortcut(it) },
        addLog = { addLog(it) },
        coroutineScope = viewModelScope
    )

    private val systemTriggerManager = SystemTriggerManager(
        context = application,
        onBatteryLevelChanged = { percentage -> automationHandler.handleBatteryChange(percentage) },
        onChargerChanged = { connected -> automationHandler.handleChargerChange(connected) },
        onHeadphonesChanged = { connected -> automationHandler.handleHeadphonesChange(connected) }
    )

    val executorEngine = ShortcutExecutorEngine(
        context = application,
        ttsManager = ttsManager,
        scope = viewModelScope,
        onLog = { /* Optionally log globally */ }
    )

    // Delegated State properties for running / execution console
    val isExecuting: Boolean get() = executorEngine.isExecuting
    val currentStep: Int get() = executorEngine.currentStep
    val totalSteps: Int get() = executorEngine.totalSteps
    val activeStepName: String get() = executorEngine.activeStepName
    val executionLogs: List<String> get() = executorEngine.executionLogs
    
    var showConsole: Boolean
        get() = executorEngine.showConsole
        set(value) { executorEngine.showConsole = value }

    // Delegated graphics settings from preferencesManager
    val isLowGraphicsQuality: Boolean get() = preferencesManager.isLowGraphicsQuality

    fun updateLowGraphicsQuality(enabled: Boolean) {
        preferencesManager.updateLowGraphicsQuality(enabled)
        addLog("⚙️ Calidad gráfica configurada en: ${if (enabled) "Baja" else "Normal"}")
    }

    // Delegated Suspended Dialog states
    val showAlertDialogState: AlertDialogState? get() = executorEngine.showAlertDialogState
    val showPromptDialogState: PromptDialogState? get() = executorEngine.showPromptDialogState

    // Selected shortcut for Editor Screen
    var selectedShortcut by mutableStateOf<Shortcut?>(null)

    init {
        // Uncaught crash handler to save system crash logs automatically
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            FileLogManager.logCrash(application, throwable)
            defaultHandler?.uncaughtException(thread, throwable)
        }

        // Register system triggers
        try {
            systemTriggerManager.register()
        } catch (e: Exception) {
            addLog("⚠️ No se pudo registrar el monitor de eventos del sistema: ${e.localizedMessage}")
            FileLogManager.logWarning(application, "ShortcutViewModel", "Register systemTriggerManager failed: ${e.localizedMessage}")
        }

        // Populate default shortcuts if database is empty
        viewModelScope.launch {
            try {
                // Wait for shortcuts to emit at least once
                val currentList = repository.allShortcuts.first()
                if (currentList.isEmpty()) {
                    insertDefaultShortcuts()
                }
            } catch (e: Exception) {
                addLog("⚠️ Error inicializando plantilla: ${e.localizedMessage}")
                FileLogManager.logWarning(application, "ShortcutViewModel", "Default shortcuts error: ${e.localizedMessage}")
            }
        }
    }

    private suspend fun insertDefaultShortcuts() = withContext(Dispatchers.IO) {
        val defaultShortcuts = DefaultShortcutsProvider.getDefaultShortcuts()
        defaultShortcuts.forEach { shortcut ->
            repository.insertShortcut(shortcut)
        }
    }

    fun runShortcut(shortcut: Shortcut) {
        executorEngine.runShortcut(shortcut)
    }

    fun addLog(message: String) {
        executorEngine.addLog(message)
    }

    fun clearLogs() {
        executorEngine.clearLogs()
    }

    // --- DB Operations ---

    fun selectShortcutForEditing(shortcut: Shortcut?) {
        selectedShortcut = shortcut
    }

    fun saveShortcut(shortcut: Shortcut) {
        viewModelScope.launch(Dispatchers.IO) {
            val savedId = if (shortcut.id == 0) {
                repository.insertShortcut(shortcut).toInt()
            } else {
                repository.updateShortcut(shortcut)
                shortcut.id
            }
            val finalShortcut = shortcut.copy(id = savedId)
            if (finalShortcut.isScheduleTriggerEnabled) {
                com.example.executor.ScheduleTriggerManager.scheduleShortcutAlarm(getApplication(), finalShortcut)
            } else {
                com.example.executor.ScheduleTriggerManager.cancelShortcutAlarm(getApplication(), finalShortcut.id)
            }
        }
    }

    fun deleteShortcut(shortcut: Shortcut) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteShortcut(shortcut)
            com.example.executor.ScheduleTriggerManager.cancelShortcutAlarm(getApplication(), shortcut.id)
            if (selectedShortcut?.id == shortcut.id) {
                selectedShortcut = null
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.shutdown()
        systemTriggerManager.unregister()
    }
}

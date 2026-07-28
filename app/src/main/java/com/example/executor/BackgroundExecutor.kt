package com.example.executor

import android.content.Context
import android.speech.tts.TextToSpeech
import android.widget.Toast
import com.example.data.Shortcut
import kotlinx.coroutines.*
import java.util.Locale

object BackgroundExecutor {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val lastExecutionTimes = java.util.concurrent.ConcurrentHashMap<Int, Long>()
    private const val COOLDOWN_MS = 2500L

    fun executeShortcutInBackground(context: Context, shortcut: Shortcut) {
        val now = System.currentTimeMillis()
        val lastTime = lastExecutionTimes[shortcut.id] ?: 0L
        if (now - lastTime < COOLDOWN_MS) {
            android.util.Log.d("BackgroundExecutor", "Ignorando ejecución duplicada de ${shortcut.name} (cooldown)")
            return
        }
        lastExecutionTimes[shortcut.id] = now

        scope.launch {
            try {
                // Show a friendly toast to the user
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context.applicationContext,
                        "Kinetix: Ejecutando \"${shortcut.name}\" en segundo plano...",
                        Toast.LENGTH_LONG
                    ).show()
                }

                // Initialize a temporary TTS engine if needed
                var tempTts: TextToSpeech? = null
                val ttsDeferred = CompletableDeferred<TextToSpeech?>()
                
                withContext(Dispatchers.Main) {
                    try {
                        tempTts = TextToSpeech(context.applicationContext) { status ->
                            if (status == TextToSpeech.SUCCESS) {
                                tempTts?.language = Locale.getDefault()
                                ttsDeferred.complete(tempTts)
                            } else {
                                ttsDeferred.complete(null)
                            }
                        }
                    } catch (e: Exception) {
                        ttsDeferred.complete(null)
                    }
                }

                val ttsEngine = withTimeoutOrNull(3000) { ttsDeferred.await() }

                val executor = ShortcutExecutor(
                    context = context.applicationContext,
                    tts = ttsEngine,
                    callbacks = BackgroundCallbacks(context)
                )

                executor.execute(shortcut.actions)

                // Clean up TTS engine on Main thread
                withContext(Dispatchers.Main) {
                    tempTts?.shutdown()
                }

                FileLogManager.logWarning(
                    context,
                    "BackgroundExecutor",
                    "Atajo \"${shortcut.name}\" ejecutado correctamente en segundo plano."
                )
            } catch (e: Exception) {
                FileLogManager.logWarning(
                    context,
                    "BackgroundExecutorError",
                    "Error al ejecutar atajo en segundo plano: ${e.localizedMessage}"
                )
            }
        }
    }
}

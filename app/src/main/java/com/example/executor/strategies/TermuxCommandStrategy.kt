package com.example.executor.strategies

import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import com.example.executor.ActionStrategy
import com.example.executor.ShortcutExecutionCallbacks

class TermuxCommandStrategy : ActionStrategy {
    override suspend fun execute(
        context: Context,
        tts: TextToSpeech?,
        callbacks: ShortcutExecutionCallbacks,
        resolvedParams: Map<String, String>,
        currentInput: String
    ): String {
        val command = resolvedParams["command"] ?: ""
        val args = resolvedParams["args"] ?: ""
        val runInBackground = resolvedParams["runInBackground"]?.toBoolean() ?: true
        
        callbacks.onLog("   ⌨️ Enviando comando a Termux: \"$command\"")
        
        try {
            val intent = Intent().apply {
                action = "com.termux.service.RUN_COMMAND"
                setClassName("com.termux", "com.termux.app.RunCommandService")
                putExtra("com.termux.execute.background", runInBackground)
                putExtra("com.termux.execute.executable", "/data/data/com.termux/files/usr/bin/bash")
                
                val arguments = if (args.isNotEmpty()) {
                    arrayOf("-c", "$command $args")
                } else {
                    arrayOf("-c", command)
                }
                putExtra("com.termux.execute.arguments", arguments)
            }
            
            context.startService(intent)
            callbacks.onLog("   ✅ Intent de comando Termux enviado con éxito.")
            callbacks.onLog("   💡 Nota: Termux debe estar instalado y el permiso 'RUN_COMMAND' activado en sus ajustes.")
        } catch (e: Exception) {
            callbacks.onLog("   ⚠️ Falló al enviar comando a Termux: ${e.localizedMessage}")
            callbacks.onLog("   💡 Asegúrate de que Termux está instalado y configurado.")
        }
        
        return currentInput
    }
}

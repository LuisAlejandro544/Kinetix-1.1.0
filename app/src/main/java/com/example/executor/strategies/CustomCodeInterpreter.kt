package com.example.executor.strategies

import android.speech.tts.TextToSpeech
import com.example.executor.ShortcutExecutionCallbacks

object CustomCodeInterpreter {
    fun execute(
        script: String,
        currentInput: String,
        tts: TextToSpeech?,
        callbacks: ShortcutExecutionCallbacks
    ): String {
        val variables = mutableMapOf<String, String>()
        variables["input"] = currentInput
        variables["resultado"] = currentInput
        
        var finalValue = currentInput
        val lines = script.split("\n")
        
        for (rawLine in lines) {
            val line = rawLine.trim()
            if (line.isEmpty() || line.startsWith("//") || line.startsWith("#")) continue
            
            try {
                when {
                    line.startsWith("PRINT", ignoreCase = true) -> {
                        val expression = line.substring(5).trim()
                        val evaluated = evaluateExpression(expression, variables)
                        callbacks.onLog("     💬 [Script PRINT] $evaluated")
                    }
                    line.startsWith("SET", ignoreCase = true) -> {
                        val assignment = line.substring(3).trim()
                        val eqIdx = assignment.indexOf('=')
                        if (eqIdx != -1) {
                            val varName = assignment.substring(0, eqIdx).trim()
                            val expr = assignment.substring(eqIdx + 1).trim()
                            val evaluated = evaluateExpression(expr, variables)
                            variables[varName] = evaluated
                            callbacks.onLog("     ⚙️ [Script SET] Variable '$varName' = \"$evaluated\"")
                        }
                    }
                    line.startsWith("RETURN", ignoreCase = true) -> {
                        val expression = line.substring(6).trim()
                        finalValue = evaluateExpression(expression, variables)
                        callbacks.onLog("     🏁 [Script RETURN] Retornando: \"$finalValue\"")
                        break
                    }
                    line.startsWith("TTS", ignoreCase = true) -> {
                        val expression = line.substring(3).trim()
                        val evaluated = evaluateExpression(expression, variables)
                        callbacks.onLog("     🗣️ [Script TTS] Pronunciando: \"$evaluated\"")
                        tts?.speak(evaluated, TextToSpeech.QUEUE_FLUSH, null, "shortcut_script_tts")
                    }
                    line.startsWith("UPPERCASE", ignoreCase = true) -> {
                        val expression = line.substring(9).trim()
                        val evaluated = evaluateExpression(expression, variables).uppercase()
                        finalValue = evaluated
                    }
                    line.startsWith("LOWERCASE", ignoreCase = true) -> {
                        val expression = line.substring(9).trim()
                        val evaluated = evaluateExpression(expression, variables).lowercase()
                        finalValue = evaluated
                    }
                    else -> {
                        finalValue = evaluateExpression(line, variables)
                    }
                }
            } catch (e: Exception) {
                callbacks.onLog("     ⚠️ Error en línea \"$line\": ${e.localizedMessage}")
            }
        }
        
        callbacks.onLog("   ✅ Script personalizado ejecutado. Resultado final: \"$finalValue\"")
        return finalValue
    }
    
    private fun evaluateExpression(expr: String, variables: Map<String, String>): String {
        var result = expr.trim()
        
        if ((result.startsWith("\"") && result.endsWith("\"")) || (result.startsWith("'") && result.endsWith("'"))) {
            return result.substring(1, result.length - 1)
        }
        
        for ((key, value) in variables) {
            result = result.replace("{$key}", value)
                           .replace(key, value)
        }
        
        if (result.contains("+")) {
            val parts = result.split("+")
            val resolvedParts = parts.map { part ->
                val trimmedPart = part.trim()
                if ((trimmedPart.startsWith("\"") && trimmedPart.endsWith("\"")) || (trimmedPart.startsWith("'") && trimmedPart.endsWith("'"))) {
                    trimmedPart.substring(1, trimmedPart.length - 1)
                } else {
                    variables[trimmedPart] ?: trimmedPart
                }
            }
            return resolvedParts.joinToString("")
        }
        
        return result
    }
}

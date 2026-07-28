package com.example.executor.strategies

import android.content.Context
import android.speech.tts.TextToSpeech
import com.example.executor.ActionStrategy
import com.example.executor.ShortcutExecutionCallbacks

class TextInputStrategy : ActionStrategy {
    override suspend fun execute(
        context: Context,
        tts: TextToSpeech?,
        callbacks: ShortcutExecutionCallbacks,
        resolvedParams: Map<String, String>,
        currentInput: String
    ): String {
        val prompt = resolvedParams["prompt"] ?: "Introduce texto:"
        val defaultValue = resolvedParams["defaultValue"] ?: ""
        callbacks.onLog("   💬 Esperando entrada del usuario...")
        val result = callbacks.onPromptInput(prompt, defaultValue)
        callbacks.onLog("   ✏️ Texto ingresado: \"$result\"")
        return result
    }
}

class TextTransformStrategy : ActionStrategy {
    override suspend fun execute(
        context: Context,
        tts: TextToSpeech?,
        callbacks: ShortcutExecutionCallbacks,
        resolvedParams: Map<String, String>,
        currentInput: String
    ): String {
        val transformType = resolvedParams["transformType"] ?: "UPPERCASE"
        callbacks.onLog("   ⚙️ Transformando \"$currentInput\" usando $transformType")
        val result = when (transformType) {
            "UPPERCASE" -> currentInput.uppercase()
            "lowercase" -> currentInput.lowercase()
            "Reverse" -> currentInput.reversed()
            "Word Count" -> currentInput.split("\\s+".toRegex()).filter { it.isNotBlank() }.size.toString()
            else -> currentInput
        }
        callbacks.onLog("   📝 Resultado: \"$result\"")
        return result
    }
}

class MathOpStrategy : ActionStrategy {
    override suspend fun execute(
        context: Context,
        tts: TextToSpeech?,
        callbacks: ShortcutExecutionCallbacks,
        resolvedParams: Map<String, String>,
        currentInput: String
    ): String {
        val operation = resolvedParams["operation"] ?: "Add"
        val operandStr = resolvedParams["operand"] ?: "1"
        val inputNum = currentInput.toDoubleOrNull() ?: 0.0
        val operandNum = operandStr.toDoubleOrNull() ?: 0.0
        
        val opSymbol = when (operation) {
            "Add" -> "+"
            "Subtract" -> "-"
            "Multiply" -> "×"
            "Divide" -> "÷"
            else -> "?"
        }
        
        callbacks.onLog("   🧮 Calculando: $inputNum $opSymbol $operandNum")
        val resultNum = when (operation) {
            "Add" -> inputNum + operandNum
            "Subtract" -> inputNum - operandNum
            "Multiply" -> inputNum * operandNum
            "Divide" -> if (operandNum != 0.0) inputNum / operandNum else 0.0
            else -> inputNum
        }
        val result = if (resultNum % 1.0 == 0.0) resultNum.toLong().toString() else resultNum.toString()
        callbacks.onLog("   📊 Resultado numérico: $result")
        return result
    }
}

class ShareTextStrategy : ActionStrategy {
    override suspend fun execute(
        context: Context,
        tts: TextToSpeech?,
        callbacks: ShortcutExecutionCallbacks,
        resolvedParams: Map<String, String>,
        currentInput: String
    ): String {
        val textToShare = resolvedParams["text"] ?: currentInput
        callbacks.onLog("   📤 Compartiendo texto: \"$textToShare\"")
        try {
            val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(android.content.Intent.EXTRA_TEXT, textToShare)
            }
            val chooserIntent = android.content.Intent.createChooser(intent, "Compartir con Kinetix").apply {
                flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(chooserIntent)
        } catch (e: Exception) {
            callbacks.onLog("⚠️ No se pudo abrir el menú de compartir: ${e.localizedMessage}")
        }
        return textToShare
    }
}

class ConditionalStrategy : ActionStrategy {
    override suspend fun execute(
        context: Context,
        tts: TextToSpeech?,
        callbacks: ShortcutExecutionCallbacks,
        resolvedParams: Map<String, String>,
        currentInput: String
    ): String {
        val value = resolvedParams["value"] ?: currentInput
        val operator = resolvedParams["operator"] ?: "Equals"
        val compareValue = resolvedParams["compareValue"] ?: ""
        val thenValue = resolvedParams["thenValue"] ?: "Sí"
        val elseValue = resolvedParams["elseValue"] ?: "No"

        callbacks.onLog("   ❓ Evaluando condición: Si \"$value\" $operator \"$compareValue\"")

        val isTrue = when (operator) {
            "Equals" -> value.equals(compareValue, ignoreCase = true)
            "NotEquals" -> !value.equals(compareValue, ignoreCase = true)
            "Contains" -> value.contains(compareValue, ignoreCase = true)
            "GreaterThan" -> {
                val valNum = value.toDoubleOrNull() ?: 0.0
                val compNum = compareValue.toDoubleOrNull() ?: 0.0
                valNum > compNum
            }
            "LessThan" -> {
                val valNum = value.toDoubleOrNull() ?: 0.0
                val compNum = compareValue.toDoubleOrNull() ?: 0.0
                valNum < compNum
            }
            else -> false
        }

        val result = if (isTrue) thenValue else elseValue
        callbacks.onLog("   ℹ️ Resultado condición: ${if (isTrue) "VERDADERO" else "FALSO"} -> Devolviendo: \"$result\"")
        return result
    }
}

class AlertDialogStrategy : ActionStrategy {
    override suspend fun execute(
        context: Context,
        tts: TextToSpeech?,
        callbacks: ShortcutExecutionCallbacks,
        resolvedParams: Map<String, String>,
        currentInput: String
    ): String {
        val title = resolvedParams["title"] ?: "Atajo"
        val message = resolvedParams["message"] ?: ""
        callbacks.onLog("   📌 Mostrando diálogo de alerta...")
        callbacks.onShowDialog(title, message)
        return message
    }
}

object TextAndMathStrategiesRegistry {
    val strategies: Map<com.example.data.ActionType, ActionStrategy> = mapOf(
        com.example.data.ActionType.TEXT_INPUT to TextInputStrategy(),
        com.example.data.ActionType.TEXT_TRANSFORM to TextTransformStrategy(),
        com.example.data.ActionType.MATH_OP to MathOpStrategy(),
        com.example.data.ActionType.SHARE_TEXT to ShareTextStrategy(),
        com.example.data.ActionType.CONDITIONAL to ConditionalStrategy(),
        com.example.data.ActionType.ALERT_DIALOG to AlertDialogStrategy()
    )
}


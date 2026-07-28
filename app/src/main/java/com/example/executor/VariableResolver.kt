package com.example.executor

object VariableResolver {
    fun resolvePlaceholders(params: Map<String, String>, currentInput: String): Map<String, String> {
        val safeInput = currentInput.take(10000) // Validate and prevent memory bloat
        return params.mapValues { (_, value) ->
            value.replace("{entrada_atajo}", safeInput)
                 .replace("\${entrada_atajo}", safeInput)
                 .replace("{resultado_anterior}", safeInput)
                 .replace("\${resultado_anterior}", safeInput)
                 .replace("{anterior}", safeInput)
                 .replace("\${anterior}", safeInput)
                 .replace("{entrada}", safeInput)
                 .replace("\${entrada}", safeInput)
                 .replace("{input}", safeInput)
                 .replace("\${input}", safeInput)
                 .replace("{resultado}", safeInput)
                 .replace("\${resultado}", safeInput)
        }
    }
}

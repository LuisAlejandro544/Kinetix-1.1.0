package com.example.data

object DefaultShortcutsProvider {
    fun getDefaultShortcuts(): List<Shortcut> {
        return listOf(
            Shortcut(
                name = "Nivel de Batería Hablado",
                description = "Consulta la batería del dispositivo, la lee en voz alta y envía una notificación.",
                iconName = "battery",
                colorHex = "#34C759", // Green
                actions = listOf(
                    ActionData(ActionType.SYSTEM_INFO, mapOf("infoType" to "Battery Level")),
                    ActionData(ActionType.SPEAK_TEXT, mapOf("text" to "Tu nivel de batería actual es de {resultado}")),
                    ActionData(ActionType.SHOW_NOTIFICATION, mapOf("title" to "Estado de Batería", "message" to "El nivel de batería actual es de {resultado}"))
                )
            ),
            Shortcut(
                name = "Gritar Texto",
                description = "Pide un texto, lo convierte a MAYÚSCULAS, lo lee en voz alta y muestra una alerta.",
                iconName = "volume",
                colorHex = "#FF9500", // Orange
                actions = listOf(
                    ActionData(ActionType.TEXT_INPUT, mapOf("prompt" to "¿Qué texto quieres gritar?", "defaultValue" to "Hola Android")),
                    ActionData(ActionType.TEXT_TRANSFORM, mapOf("transformType" to "UPPERCASE")),
                    ActionData(ActionType.SPEAK_TEXT, mapOf("text" to "{resultado}")),
                    ActionData(ActionType.ALERT_DIALOG, mapOf("title" to "Texto Gritado", "message" to "Se ha pronunciado: {resultado}"))
                )
            ),
            Shortcut(
                name = "Compartir Mi Modelo",
                description = "Obtiene el modelo de tu teléfono y abre el menú para compartirlo.",
                iconName = "share",
                colorHex = "#007AFF", // Blue
                actions = listOf(
                    ActionData(ActionType.SYSTEM_INFO, mapOf("infoType" to "Device Model")),
                    ActionData(ActionType.SHARE_TEXT, mapOf("text" to "📱 ¡Hola! Estoy usando un {resultado} con la increíble app Kinetix."))
                )
            ),
            Shortcut(
                name = "Calculadora Inteligente",
                description = "Pide un número base, le suma 5, lo multiplica por 10 y te muestra el resultado final.",
                iconName = "calculator",
                colorHex = "#AF52DE", // Purple
                actions = listOf(
                    ActionData(ActionType.TEXT_INPUT, mapOf("prompt" to "Ingresa un número inicial:", "defaultValue" to "5")),
                    ActionData(ActionType.MATH_OP, mapOf("operation" to "Add", "operand" to "5")),
                    ActionData(ActionType.MATH_OP, mapOf("operation" to "Multiply", "operand" to "10")),
                    ActionData(ActionType.ALERT_DIALOG, mapOf("title" to "Resultado Matemático", "message" to "El resultado final de (X + 5) * 10 es: {resultado}"))
                )
            )
        )
    }
}

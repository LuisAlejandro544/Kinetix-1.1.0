package com.example.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class ActionType {
    SPEAK_TEXT,
    SHOW_NOTIFICATION,
    VIBRATE,
    OPEN_URL,
    TEXT_INPUT,
    TEXT_TRANSFORM,
    ALERT_DIALOG,
    SYSTEM_INFO,
    MATH_OP,
    SHARE_TEXT,
    CONDITIONAL,
    OPEN_APP,
    SET_VOLUME,
    SET_RINGER_MODE,
    WRITE_FILE,
    READ_FILE,
    APPEND_FILE,
    SET_BRIGHTNESS,
    ACCESSIBILITY_ACTION,
    PLAY_SOUND,
    EXEC_JAVASCRIPT,
    TERMUX_COMMAND,
    CUSTOM_CODE,
    BACKGROUND_CAMERA_CAPTURE,
    SIMULATE_GESTURES,
    CLIPBOARD_SILENT,
    HTTP_REQUEST;

    val displayName: String
        get() = when (this) {
            SPEAK_TEXT -> "Pronunciar texto (TTS)"
            SHOW_NOTIFICATION -> "Mostrar notificación"
            VIBRATE -> "Vibrar dispositivo"
            OPEN_URL -> "Abrir página web"
            TEXT_INPUT -> "Solicitar texto"
            TEXT_TRANSFORM -> "Transformar texto"
            ALERT_DIALOG -> "Mostrar alerta"
            SYSTEM_INFO -> "Obtener info del sistema"
            MATH_OP -> "Operación matemática"
            SHARE_TEXT -> "Compartir texto"
            CONDITIONAL -> "Lógica condicional (Si / Entonces)"
            OPEN_APP -> "Abrir aplicación"
            SET_VOLUME -> "Establecer volumen"
            SET_RINGER_MODE -> "Cambiar modo de sonido"
            WRITE_FILE -> "Escribir archivo"
            READ_FILE -> "Leer archivo"
            APPEND_FILE -> "Añadir a archivo"
            SET_BRIGHTNESS -> "Ajustar brillo de pantalla"
            ACCESSIBILITY_ACTION -> "Acción de accesibilidad"
            PLAY_SOUND -> "Reproducir sonido"
            EXEC_JAVASCRIPT -> "Ejecutar JavaScript (QuickJS)"
            TERMUX_COMMAND -> "Ejecutar comando en Termux"
            CUSTOM_CODE -> "Código Personalizado (Custom Block)"
            BACKGROUND_CAMERA_CAPTURE -> "Capturar foto de cámara"
            SIMULATE_GESTURES -> "Simular toque/desplazamiento"
            CLIPBOARD_SILENT -> "Portapapeles silencioso"
            HTTP_REQUEST -> "Petición HTTP / Webhook"
        }

    val icon: ImageVector
        get() = when (this) {
            SPEAK_TEXT -> Icons.AutoMirrored.Filled.VolumeUp
            SHOW_NOTIFICATION -> Icons.Default.Notifications
            VIBRATE -> Icons.Default.Vibration
            OPEN_URL -> Icons.Default.Language
            TEXT_INPUT -> Icons.Default.Edit
            TEXT_TRANSFORM -> Icons.Default.Transform
            ALERT_DIALOG -> Icons.Default.Info
            SYSTEM_INFO -> Icons.Default.Settings
            MATH_OP -> Icons.Default.Calculate
            SHARE_TEXT -> Icons.Default.Share
            CONDITIONAL -> Icons.AutoMirrored.Filled.CallSplit
            OPEN_APP -> Icons.Default.Apps
            SET_VOLUME -> Icons.AutoMirrored.Filled.VolumeDown
            SET_RINGER_MODE -> Icons.AutoMirrored.Filled.VolumeOff
            WRITE_FILE -> Icons.Default.Save
            READ_FILE -> Icons.Default.Description
            APPEND_FILE -> Icons.AutoMirrored.Filled.NoteAdd
            SET_BRIGHTNESS -> Icons.Default.LightMode
            ACCESSIBILITY_ACTION -> Icons.Default.Accessibility
            PLAY_SOUND -> Icons.Default.MusicNote
            EXEC_JAVASCRIPT -> Icons.Default.Code
            TERMUX_COMMAND -> Icons.Default.Terminal
            CUSTOM_CODE -> Icons.Default.DeveloperMode
            BACKGROUND_CAMERA_CAPTURE -> Icons.Default.PhotoCamera
            SIMULATE_GESTURES -> Icons.Default.TouchApp
            CLIPBOARD_SILENT -> Icons.Default.ContentPaste
            HTTP_REQUEST -> Icons.Default.CloudSync
        }

    val description: String
        get() = when (this) {
            SPEAK_TEXT -> "Lee texto en voz alta utilizando el motor de síntesis de voz de Android."
            SHOW_NOTIFICATION -> "Muestra una notificación en la barra de estado de Android."
            VIBRATE -> "Hace vibrar el teléfono durante los milisegundos especificados."
            OPEN_URL -> "Abre una dirección web o URL en el navegador de internet."
            TEXT_INPUT -> "Muestra un diálogo solicitando una entrada de texto al usuario."
            TEXT_TRANSFORM -> "Modifica el texto recibido (MAYÚSCULAS, minúsculas, reverso)."
            ALERT_DIALOG -> "Muestra un diálogo pop-up de alerta con un mensaje."
            SYSTEM_INFO -> "Obtiene información en tiempo real como batería o modelo del dispositivo."
            MATH_OP -> "Aplica sumas, restas, multiplicaciones o divisiones al número de entrada."
            SHARE_TEXT -> "Abre el diálogo nativo de compartir para enviar el texto a otras aplicaciones."
            CONDITIONAL -> "Evalúa una condición de texto o número. Si es verdadera, devuelve un valor; si no, devuelve otro."
            OPEN_APP -> "Espera el tiempo seleccionado y luego abre una aplicación instalada en el dispositivo."
            SET_VOLUME -> "Cambia el volumen de un canal específico (Multimedia, Timbre, Alarma, etc.) a un porcentaje del 0 al 100."
            SET_RINGER_MODE -> "Cambia entre los modos de sonido: Sonido, Vibrar, o Silenciar (No Molestar)."
            WRITE_FILE -> "Guarda texto en un archivo local en el almacenamiento interno de la app."
            READ_FILE -> "Lee el contenido completo de un archivo local de texto."
            APPEND_FILE -> "Añade texto o registros de logs al final de un archivo local de texto."
            SET_BRIGHTNESS -> "Ajusta el brillo general de la pantalla (0% a 100%). Requiere permisos del sistema."
            ACCESSIBILITY_ACTION -> "Ejecuta acciones de accesibilidad como Atrás, Inicio, Abrir Notificaciones, Ajustes Rápidos o Bloquear Pantalla."
            PLAY_SOUND -> "Reproduce un sonido o tono del sistema al estilo de los atajos de iPhone."
            EXEC_JAVASCRIPT -> "Ejecuta código JavaScript usando un motor QuickJS embebido ultraligero y seguro."
            TERMUX_COMMAND -> "Envía y ejecuta comandos en Termux utilizando su API de integración y emisión de scripts."
            CUSTOM_CODE -> "Crea una acción totalmente personalizada escribiendo un script con lógica, variables y salidas."
            BACKGROUND_CAMERA_CAPTURE -> "Captura una foto de la cámara frontal o trasera en segundo plano de manera silenciosa."
            SIMULATE_GESTURES -> "Simula un toque o deslizamiento (swipe) en coordenadas específicas de la pantalla."
            CLIPBOARD_SILENT -> "Lee o escribe texto en el portapapeles del sistema de manera silenciosa."
            HTTP_REQUEST -> "Realiza solicitudes HTTP (GET, POST, PUT, DELETE, PATCH) con cabeceras y cuerpo personalizables a APIs o webhooks."
        }

    val defaultParams: Map<String, String>
        get() = when (this) {
            SPEAK_TEXT -> mapOf("text" to "{resultado}", "speechRate" to "1.0")
            SHOW_NOTIFICATION -> mapOf("title" to "Kinetix", "message" to "{resultado}")
            VIBRATE -> mapOf("duration" to "500")
            OPEN_URL -> mapOf("url" to "https://google.com")
            TEXT_INPUT -> mapOf("prompt" to "Escribe un mensaje para continuar:", "defaultValue" to "")
            TEXT_TRANSFORM -> mapOf("transformType" to "UPPERCASE")
            ALERT_DIALOG -> mapOf("title" to "Alerta de Kinetix", "message" to "{resultado}")
            SYSTEM_INFO -> mapOf("infoType" to "Battery Level")
            MATH_OP -> mapOf("operation" to "Add", "operand" to "1")
            SHARE_TEXT -> mapOf("text" to "{resultado}")
            CONDITIONAL -> mapOf("value" to "{resultado}", "operator" to "Equals", "compareValue" to "", "thenValue" to "Sí", "elseValue" to "No")
            OPEN_APP -> mapOf("packageName" to "", "appName" to "", "delay" to "3")
            SET_VOLUME -> mapOf("streamType" to "Music", "volumePercent" to "50")
            SET_RINGER_MODE -> mapOf("ringerMode" to "Vibrate")
            WRITE_FILE -> mapOf("fileName" to "kinetix_datos.txt", "content" to "{resultado}")
            READ_FILE -> mapOf("fileName" to "kinetix_datos.txt")
            APPEND_FILE -> mapOf("fileName" to "kinetix_log.txt", "content" to "{resultado}")
            SET_BRIGHTNESS -> mapOf("brightnessPercent" to "70")
            ACCESSIBILITY_ACTION -> mapOf("actionType" to "Back")
            PLAY_SOUND -> mapOf("soundType" to "Beep")
            EXEC_JAVASCRIPT -> mapOf("code" to "const input = '{resultado}';\n// Tu código JS aquí. Retorna el resultado final al final del script.\ninput.toUpperCase() + ' (QuickJS)';")
            TERMUX_COMMAND -> mapOf("command" to "echo 'Hola desde Kinetix!'; date", "args" to "", "runInBackground" to "true")
            CUSTOM_CODE -> mapOf("script" to "PRINT 'Resultado anterior: ' + {resultado};\nSET output = 'Resultado personalizado: ' + {resultado};\nRETURN output;")
            BACKGROUND_CAMERA_CAPTURE -> mapOf("cameraType" to "BACK")
            SIMULATE_GESTURES -> mapOf("gestureType" to "TAP", "x1" to "500", "y1" to "1000", "x2" to "500", "y2" to "500", "duration" to "300")
            CLIPBOARD_SILENT -> mapOf("operation" to "WRITE", "text" to "{resultado}")
            HTTP_REQUEST -> mapOf("url" to "https://httpbin.org/get", "method" to "GET", "headers" to "Content-Type: application/json", "body" to "", "timeout" to "10")
        }

    companion object {
        const val input = "\${input}"
        const val friendlyInput = "{resultado}"
    }
}

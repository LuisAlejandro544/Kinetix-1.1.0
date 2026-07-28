package com.example.executor.strategies

import android.content.Context
import android.speech.tts.TextToSpeech
import com.example.executor.ActionStrategy
import com.example.executor.ShortcutExecutionCallbacks

class SetRingerModeStrategy : ActionStrategy {
    override suspend fun execute(
        context: Context,
        tts: TextToSpeech?,
        callbacks: ShortcutExecutionCallbacks,
        resolvedParams: Map<String, String>,
        currentInput: String
    ): String {
        val modeStr = resolvedParams["ringerMode"] ?: "Vibrate"
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? android.media.AudioManager
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? android.app.NotificationManager

        if (audioManager != null) {
            callbacks.onLog("   🔇 Cambiando modo de sonido a: $modeStr")
            try {
                when (modeStr) {
                    "Normal" -> {
                        audioManager.ringerMode = android.media.AudioManager.RINGER_MODE_NORMAL
                        callbacks.onLog("   🔔 Modo de sonido establecido a Normal.")
                    }
                    "Vibrate" -> {
                        audioManager.ringerMode = android.media.AudioManager.RINGER_MODE_VIBRATE
                        callbacks.onLog("   📳 Modo de sonido establecido a Vibración.")
                    }
                    "Silent" -> {
                        val hasAccess = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            notificationManager?.isNotificationPolicyAccessGranted == true
                        } else {
                            true
                        }

                        if (hasAccess) {
                            audioManager.ringerMode = android.media.AudioManager.RINGER_MODE_SILENT
                            callbacks.onLog("   🔕 Modo de sonido establecido a Silencio.")
                        } else {
                            callbacks.onLog("   ⚠️ Falta permiso de \"No Molestar\" para silenciar completamente.")
                            callbacks.onLog("   💡 Intentando silenciar bajando los volúmenes a 0 como alternativa...")
                            try {
                                audioManager.setStreamVolume(android.media.AudioManager.STREAM_RING, 0, android.media.AudioManager.FLAG_SHOW_UI)
                                audioManager.setStreamVolume(android.media.AudioManager.STREAM_NOTIFICATION, 0, 0)
                                callbacks.onLog("   🔇 Volúmenes de Timbre y Notificaciones reducidos a 0.")
                            } catch (ex: Exception) {
                                callbacks.onLog("   ⚠️ No se pudo silenciar mediante volúmenes: ${ex.localizedMessage}")
                            }
                        }
                    }
                }
            } catch (e: SecurityException) {
                callbacks.onLog("   ⚠️ Error de seguridad al cambiar modo: ${e.localizedMessage}")
                callbacks.onLog("   💡 Requiere otorgar el permiso de \"Acceso a No Molestar\".")
            } catch (e: Exception) {
                callbacks.onLog("   ⚠️ Error al cambiar modo: ${e.localizedMessage}")
            }
        } else {
            callbacks.onLog("   ⚠️ Servicio de Audio no disponible.")
        }
        return currentInput
    }
}

package com.example.executor.strategies

import android.content.Context
import android.speech.tts.TextToSpeech
import com.example.executor.ActionStrategy
import com.example.executor.ShortcutExecutionCallbacks

class ShowNotificationStrategy : ActionStrategy {
    override suspend fun execute(
        context: Context,
        tts: TextToSpeech?,
        callbacks: ShortcutExecutionCallbacks,
        resolvedParams: Map<String, String>,
        currentInput: String
    ): String {
        val title = resolvedParams["title"] ?: "Kinetix"
        val message = resolvedParams["message"] ?: ""
        callbacks.onLog("   🔔 Notificación: \"$title\" -> \"$message\"")
        
        val channelId = "atajos_notification_channel"
        val notificationId = System.currentTimeMillis().toInt()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(
                channelId,
                "Ejecución de Kinetix",
                android.app.NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones de las automatizaciones de Kinetix"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val builder = androidx.core.app.NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(androidx.core.app.NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        try {
            notificationManager.notify(notificationId, builder.build())
        } catch (e: Exception) {
            callbacks.onLog("⚠️ Error: No se pudo mostrar la notificación. Asegúrate de tener los permisos habilitados.")
        }
        return message
    }
}

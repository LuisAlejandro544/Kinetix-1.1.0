package com.example.executor.strategies

import android.content.Context
import android.speech.tts.TextToSpeech
import com.example.executor.ActionStrategy
import com.example.executor.ShortcutExecutionCallbacks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class HttpRequestStrategy : ActionStrategy {
    override suspend fun execute(
        context: Context,
        tts: TextToSpeech?,
        callbacks: ShortcutExecutionCallbacks,
        resolvedParams: Map<String, String>,
        currentInput: String
    ): String = withContext(Dispatchers.IO) {
        val urlString = resolvedParams["url"] ?: "https://httpbin.org/get"
        val method = (resolvedParams["method"] ?: "GET").uppercase()
        val headersString = resolvedParams["headers"] ?: ""
        val body = resolvedParams["body"] ?: ""
        val timeoutSec = resolvedParams["timeout"]?.toIntOrNull() ?: 10

        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = method
            connection.connectTimeout = timeoutSec * 1000
            connection.readTimeout = timeoutSec * 1000

            // Parse headers
            if (headersString.isNotBlank()) {
                headersString.split("\n").forEach { line ->
                    val parts = line.split(":", limit = 2)
                    if (parts.size == 2) {
                        connection.setRequestProperty(parts[0].trim(), parts[1].trim())
                    }
                }
            }

            // Write Body for POST, PUT, PATCH, DELETE if present
            if (body.isNotEmpty() && method in listOf("POST", "PUT", "PATCH", "DELETE")) {
                connection.doOutput = true
                OutputStreamWriter(connection.outputStream, "UTF-8").use { writer ->
                    writer.write(body)
                    writer.flush()
                }
            }

            val responseCode = connection.responseCode
            val inputStream = if (responseCode in 200..299) {
                connection.inputStream
            } else {
                connection.errorStream ?: connection.inputStream
            }

            val responseText = BufferedReader(InputStreamReader(inputStream, "UTF-8")).use { reader ->
                reader.readText()
            }

            connection.disconnect()
            if (responseText.isNotBlank()) responseText else "HTTP $responseCode"
        } catch (e: Exception) {
            "Error HTTP: ${e.localizedMessage ?: "Falló la solicitud"}"
        }
    }
}

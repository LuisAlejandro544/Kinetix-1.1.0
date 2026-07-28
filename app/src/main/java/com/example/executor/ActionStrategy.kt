package com.example.executor

import android.content.Context
import android.speech.tts.TextToSpeech

interface ActionStrategy {
    suspend fun execute(
        context: Context,
        tts: TextToSpeech?,
        callbacks: ShortcutExecutionCallbacks,
        resolvedParams: Map<String, String>,
        currentInput: String
    ): String
}

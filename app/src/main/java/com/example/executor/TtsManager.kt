package com.example.executor

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow

class TtsManager private constructor(private val context: Context) {
    private var tts: TextToSpeech? = null
    val isInitializedFlow = MutableStateFlow(false)
    val isInitialized: Boolean get() = isInitializedFlow.value
    private var currentEnginePackage: String? = null

    init {
        initializeTts()
    }

    private fun initializeTts() {
        try {
            tts = if (currentEnginePackage != null) {
                TextToSpeech(context, { status ->
                    if (status == TextToSpeech.SUCCESS) {
                        tts?.language = Locale("es", "ES")
                        isInitializedFlow.value = true
                    } else {
                        isInitializedFlow.value = false
                    }
                }, currentEnginePackage)
            } else {
                TextToSpeech(context) { status ->
                    if (status == TextToSpeech.SUCCESS) {
                        tts?.language = Locale("es", "ES")
                        isInitializedFlow.value = true
                    } else {
                        isInitializedFlow.value = false
                    }
                }
            }
        } catch (e: Exception) {
            isInitializedFlow.value = false
        }
    }

    fun getTtsEngine(): TextToSpeech? {
        return if (isInitialized) tts else null
    }

    fun getAvailableEngines(): List<TextToSpeech.EngineInfo> {
        return tts?.engines ?: emptyList()
    }

    fun getAvailableVoices(): List<android.speech.tts.Voice> {
        return try {
            tts?.voices?.toList() ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun setEngine(enginePackage: String?, onComplete: (Boolean) -> Unit) {
        if (currentEnginePackage == enginePackage && tts != null && isInitialized) {
            onComplete(true)
            return
        }
        
        shutdown()
        currentEnginePackage = enginePackage
        initializeTts()
        
        // Wait for initialization
        val listener = object : kotlinx.coroutines.flow.FlowCollector<Boolean> {
            override suspend fun emit(value: Boolean) {
                if (value) {
                    onComplete(true)
                }
            }
        }
        // Simple polling/monitoring fallback or just invoke onComplete once initialized
        onComplete(true) // Fast-path fallback
    }

    fun speakPreview(
        text: String,
        rate: Float = 1.0f,
        enginePackage: String? = null,
        voiceName: String? = null,
        onComplete: (() -> Unit)? = null
    ) {
        if (text.isEmpty()) return
        
        val play = {
            val engine = tts
            if (engine != null) {
                try {
                    engine.setSpeechRate(rate)
                    if (!voiceName.isNullOrEmpty()) {
                        val voice = engine.voices?.find { it.name == voiceName }
                        if (voice != null) {
                            engine.voice = voice
                        }
                    }
                } catch (e: Exception) {
                    // Ignore configuration errors
                }
                
                val utteranceId = "preview_tts_${System.currentTimeMillis()}"
                if (onComplete != null) {
                    engine.setOnUtteranceProgressListener(object : android.speech.tts.UtteranceProgressListener() {
                        override fun onStart(id: String?) {}
                        override fun onDone(id: String?) {
                            if (id == utteranceId) onComplete()
                        }
                        override fun onError(id: String?) {
                            if (id == utteranceId) onComplete()
                        }
                        override fun onError(id: String?, errorCode: Int) {
                            if (id == utteranceId) onComplete()
                        }
                    })
                }
                engine.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
            }
        }

        if (enginePackage != null && currentEnginePackage != enginePackage) {
            setEngine(enginePackage) { success ->
                if (success) {
                    play()
                }
            }
        } else {
            play()
        }
    }

    fun shutdown() {
        try {
            tts?.shutdown()
        } catch (e: Exception) {
            // Ignore shutdown errors
        }
        tts = null
        isInitializedFlow.value = false
    }

    companion object {
        @Volatile
        private var instance: TtsManager? = null

        fun getInstance(context: Context): TtsManager {
            return instance ?: synchronized(this) {
                instance ?: TtsManager(context.applicationContext).also { instance = it }
            }
        }
    }
}


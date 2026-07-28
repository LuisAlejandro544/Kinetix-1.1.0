package com.example.ui.preferences

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class KinetixPreferencesManager(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences("kinetix_prefs", Context.MODE_PRIVATE)
    
    var isLowGraphicsQuality by mutableStateOf(prefs.getBoolean("low_graphics_quality", false))
        private set

    fun updateLowGraphicsQuality(enabled: Boolean) {
        isLowGraphicsQuality = enabled
        prefs.edit().putBoolean("low_graphics_quality", enabled).apply()
    }
}

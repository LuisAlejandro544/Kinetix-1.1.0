package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.executor.KinetixForegroundService
import com.example.ui.ShortcutViewModel
import com.example.ui.navigation.AppNavigation
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Always ensure background monitoring service is running
        KinetixForegroundService.startService(this)

        val prefs = getSharedPreferences("kinetix_prefs", MODE_PRIVATE)
        val isOnboardingCompleted = prefs.getBoolean("onboarding_completed", false)

        setContent {
            MyApplicationTheme {
                val viewModel: ShortcutViewModel = viewModel()
                AppNavigation(
                    viewModel = viewModel,
                    prefs = prefs,
                    isOnboardingCompleted = isOnboardingCompleted
                )
            }
        }
    }
}

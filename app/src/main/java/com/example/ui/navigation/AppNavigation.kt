package com.example.ui.navigation

import android.content.SharedPreferences
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.ui.ShortcutViewModel
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.ShortcutEditorScreen
import com.example.ui.screens.ShortcutListScreen

enum class Screen {
    ONBOARDING, LIST, EDITOR, SETTINGS
}

@Composable
fun AppNavigation(
    viewModel: ShortcutViewModel,
    prefs: SharedPreferences,
    isOnboardingCompleted: Boolean,
    modifier: Modifier = Modifier
) {
    var currentScreen by remember { 
        mutableStateOf(if (isOnboardingCompleted) Screen.LIST else Screen.ONBOARDING) 
    }

    Surface(modifier = modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = {
                fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
            },
            label = "screen_transition"
        ) { screen ->
            when (screen) {
                Screen.ONBOARDING -> {
                    com.example.ui.screens.OnboardingScreen(
                        onFinished = {
                            prefs.edit().putBoolean("onboarding_completed", true).apply()
                            currentScreen = Screen.LIST
                        }
                    )
                }
                Screen.LIST -> {
                    ShortcutListScreen(
                        viewModel = viewModel,
                        onNavigateToEditor = { currentScreen = Screen.EDITOR },
                        onNavigateToSettings = { currentScreen = Screen.SETTINGS }
                    )
                }
                Screen.EDITOR -> {
                    ShortcutEditorScreen(
                        viewModel = viewModel,
                        onNavigateBack = { currentScreen = Screen.LIST }
                    )
                }
                Screen.SETTINGS -> {
                    SettingsScreen(
                        viewModel = viewModel,
                        onNavigateBack = { currentScreen = Screen.LIST }
                    )
                }
            }
        }
    }
}

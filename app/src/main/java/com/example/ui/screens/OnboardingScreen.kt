package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.R
import com.example.executor.ShortcutAccessibilityService
import com.example.ui.screens.onboarding.WelcomeStep
import com.example.ui.screens.onboarding.FeaturesStep
import com.example.ui.screens.onboarding.PermissionsStep
import com.example.ui.screens.onboarding.OnboardingHeader
import com.example.ui.screens.onboarding.OnboardingStepIndicator
import com.example.ui.screens.onboarding.OnboardingNavigationButtons

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit
) {
    var currentStep by remember { mutableStateOf(0) }
    val totalSteps = 3
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Upper accent glow
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header
            OnboardingHeader(
                currentStep = currentStep,
                totalSteps = totalSteps,
                onFinished = onFinished
            )

            // Body with animated transitions
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = currentStep,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
                    label = "onboarding_step_transition"
                ) { step ->
                    when (step) {
                        0 -> WelcomeStep()
                        1 -> FeaturesStep()
                        2 -> PermissionsStep()
                    }
                }
            }

            // Footer with indicators and buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Indicators
                OnboardingStepIndicator(
                    currentStep = currentStep,
                    totalSteps = totalSteps
                )

                // Action Buttons
                OnboardingNavigationButtons(
                    currentStep = currentStep,
                    totalSteps = totalSteps,
                    onBack = { currentStep-- },
                    onNext = {
                        if (currentStep < totalSteps - 1) {
                            currentStep++
                        } else {
                            onFinished()
                        }
                    }
                )
            }
        }
    }
}

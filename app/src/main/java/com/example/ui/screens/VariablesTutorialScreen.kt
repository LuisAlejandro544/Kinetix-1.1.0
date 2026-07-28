package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.tutorial.StepIntroduction
import com.example.ui.screens.tutorial.StepInteractiveSimulator
import com.example.ui.screens.tutorial.StepAvoidChainingError

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VariablesTutorialScreen(
    onDismiss: () -> Unit,
    onComplete: () -> Unit
) {
    var currentStep by remember { mutableStateOf(0) }
    val totalSteps = 3

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Guía de Variables",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar tutorial"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Interactive content with animations
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    AnimatedContent(
                        targetState = currentStep,
                        transitionSpec = {
                            if (targetState > initialState) {
                                (slideInHorizontally { width -> width } + fadeIn()) togetherWith
                                        (slideOutHorizontally { width -> -width } + fadeOut())
                            } else {
                                (slideInHorizontally { width -> -width } + fadeIn()) togetherWith
                                        (slideOutHorizontally { width -> width } + fadeOut())
                            }
                        },
                        label = "tutorial_step_transition"
                    ) { step ->
                        when (step) {
                            0 -> StepIntroduction()
                            1 -> StepInteractiveSimulator()
                            2 -> StepAvoidChainingError()
                        }
                    }
                }

                // Footer indicators and navigation
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Page Indicator dots
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(totalSteps) { index ->
                            val isSelected = index == currentStep
                            val width by animateFloatAsState(
                                targetValue = if (isSelected) 24f else 8f,
                                label = "dot_width"
                            )
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .size(width = width.dp, height = 8.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                                    )
                            )
                        }
                    }

                    // Navigation Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (currentStep > 0) {
                            OutlinedButton(
                                onClick = { currentStep-- },
                                modifier = Modifier
                                    .height(50.dp)
                                    .testTag("tutorial_back_button")
                             ) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Atrás")
                            }
                        } else {
                            Spacer(modifier = Modifier.width(1.dp))
                        }

                        Button(
                            onClick = {
                                if (currentStep < totalSteps - 1) {
                                    currentStep++
                                } else {
                                    onComplete()
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = if (currentStep > 0) 12.dp else 0.dp)
                                .height(50.dp)
                                .testTag("tutorial_next_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                text = if (currentStep == totalSteps - 1) "¡Entendido!" else "Siguiente",
                                fontWeight = FontWeight.Bold
                            )
                            if (currentStep < totalSteps - 1) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                            }
                        }
                    }
                }
            }
        }
    }
}

package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ShortcutViewModel

@Composable
fun ConsoleDrawer(viewModel: ShortcutViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
            .clickable(enabled = !viewModel.isExecuting) { viewModel.clearLogs() }
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.65f)
                .testTag("console_drawer_card"),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)) // Slate 800 (Dark theme)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header console
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Consola de Ejecución",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                        if (viewModel.isExecuting) {
                            Text(
                                "Paso ${viewModel.currentStep}/${viewModel.totalSteps}: ${viewModel.activeStepName}",
                                color = Color(0xFF38BDF8), // Light Blue
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold
                            )
                        } else {
                            Text(
                                "Ejecución finalizada",
                                color = Color(0xFF34D399), // Mint Green
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    if (viewModel.isExecuting) {
                        CircularProgressIndicator(
                            color = Color(0xFF38BDF8),
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        IconButton(
                            onClick = { viewModel.clearLogs() },
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.1f), CircleShape)
                                .size(32.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = {
                        if (viewModel.totalSteps > 0) {
                            viewModel.currentStep.toFloat() / viewModel.totalSteps.toFloat()
                        } else 0f
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = Color(0xFF38BDF8),
                    trackColor = Color.White.copy(alpha = 0.1f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Console output
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0F172A)) // Slate 900
                        .padding(12.dp)
                ) {
                    androidx.compose.foundation.lazy.LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        reverseLayout = true
                    ) {
                        items(viewModel.executionLogs.reversed()) { log ->
                            Text(
                                text = log,
                                color = if (log.startsWith("❌")) Color(0xFFF87171)
                                else if (log.startsWith("⚠️")) Color(0xFFFBBF24)
                                else if (log.startsWith("🎉") || log.startsWith("✅")) Color(0xFF34D399)
                                else Color(0xFFE2E8F0),
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.clearLogs() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("close_console_button"),
                    enabled = !viewModel.isExecuting,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF38BDF8),
                        contentColor = Color(0xFF0F172A)
                    )
                ) {
                    Text(
                        if (viewModel.isExecuting) "Ejecutando..." else "Aceptar",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

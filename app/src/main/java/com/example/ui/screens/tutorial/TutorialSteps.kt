package com.example.ui.screens.tutorial

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StepIntroduction() {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.05f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Hub,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(64.dp)
            )
        }

        Text(
            text = "¿Cómo viajan los datos?",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "En Kinetix, cada acción de tu atajo produce un resultado que la siguiente acción puede leer y utilizar. A esto lo llamamos el flujo de datos.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 22.sp
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Ejemplo visual:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                // Visual steps flow
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.BatteryChargingFull, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text("Paso 1: Obtener Batería", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Text("Salida: \"9%\"", fontSize = 10.sp, color = MaterialTheme.colorScheme.primary)
                        }
                    }

                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.outline)

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text("Paso 2: Hablar (TTS)", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Text("Lee: \"9%\"", fontSize = 10.sp, color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                }

                Text(
                    text = "El resultado obtenido en el Paso 1 se convierte automáticamente en el valor de {resultado} que el Paso 2 lee.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun StepInteractiveSimulator() {
    val scrollState = rememberScrollState()
    var isLinked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "El interruptor de Enlace 🔗",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "El interruptor 'Enlazar con el resultado del paso anterior' agrega la variable {resultado} al final de tu texto de forma invisible.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 22.sp
        )

        // Interactive Box
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Pruébalo en vivo:",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                // Input box simulation
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Texto en tu acción:", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                    Text(
                        text = "Tu batería es" + if (isLinked) " {resultado}" else "",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Interactive Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = if (isLinked) Icons.Default.Link else Icons.Default.LinkOff,
                            contentDescription = null,
                            tint = if (isLinked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                        )
                        Text(
                            "Enlazar con paso anterior",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Switch(
                        checked = isLinked,
                        onCheckedChange = { isLinked = it }
                    )
                }

                // Output Result Simulator
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                                    MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
                                )
                            )
                        )
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("🗣️ Lo que hablará el celular (Resultado real):", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    Text(
                        text = if (isLinked) "\"Tu batería es 9%\"" else "\"Tu batería es\"",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun StepAvoidChainingError() {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "⚠️ Evita el error de encadenamiento",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Un error muy común es dejar el interruptor de Enlace 🔗 activado en pasos consecutivos donde no lo necesitas. Esto hace que se acumule el texto del paso anterior.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 20.sp
        )

        // Comparison Cards
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Incorrect Case
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.15f)
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Cancel, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                        Text("Configuración Incorrecta ❌", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "• Paso 2 (Enlace ACTIVO 🔗): \"Tu batería es\"\n  ➔ Dice: \"Tu batería es 9%\"\n" +
                                "• Paso 3 (Enlace ACTIVO 🔗): \"Celular cargando\"\n  ➔ Dice: \"Celular cargando Tu batería es 9%\" ⚠️",
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            // Correct Case
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1B5E20).copy(alpha = 0.08f)
                ),
                border = BorderStroke(1.dp, Color(0xFF4CAF50).copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF4CAF50))
                        Text("Configuración Correcta ✅", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32), fontSize = 13.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "• Paso 2 (Enlace ACTIVO 🔗): \"Tu batería es\"\n  ➔ Dice: \"Tu batería es 9%\"\n" +
                                "• Paso 3 (Enlace APAGADO 🚫): \"Celular cargando\"\n  ➔ Dice: \"Celular cargando\" (¡Mensaje limpio!) ✨",
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "💡 Regla de oro: Si quieres que un paso diga o haga algo totalmente nuevo e independiente, asegúrate de DESACTIVAR el interruptor de Enlace 🔗.",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            lineHeight = 20.sp
        )
    }
}

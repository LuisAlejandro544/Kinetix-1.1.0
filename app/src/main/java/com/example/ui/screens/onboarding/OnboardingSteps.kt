package com.example.ui.screens.onboarding

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.executor.ShortcutAccessibilityService

@Composable
fun WelcomeStep() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Centered Hero area with a glowing technical layout (completely native)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            Color.Transparent
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Draw abstract tech/automation concentric dashed paths
            androidx.compose.foundation.Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                val strokeColor = Color.White.copy(alpha = 0.05f)
                
                // Concentric circles representing orbits/cycles
                drawCircle(
                    color = strokeColor,
                    radius = 90.dp.toPx(),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = 1.dp.toPx(),
                        pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                    )
                )
                drawCircle(
                    color = strokeColor,
                    radius = 60.dp.toPx(),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = 1.dp.toPx(),
                        pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )
                )
            }

            // Layered logo (larger, higher contrast)
            Box(
                modifier = Modifier.size(130.dp),
                contentAlignment = Alignment.Center
            ) {
                // Bottom Diamond Card (Cyan-Blue)
                Box(
                    modifier = Modifier
                        .offset(y = 16.dp)
                        .size(72.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.18f), RoundedCornerShape(18.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF00F2FE), Color(0xFF005CFF))
                            )
                        )
                )

                // Top Diamond Card (Pink-Orange with slight alpha)
                Box(
                    modifier = Modifier
                        .offset(y = (-16).dp)
                        .size(72.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFFFF0844), Color(0xFFFFB199))
                            )
                        )
                )

                // "PRE-ALPHA" Badge overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = 12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFFF2D55))
                        .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "PRE-ALPHA",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Bienvenido a Kinetix",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "La automatización definitiva para Android. Diseña secuencias de comandos, controla funciones del sistema y crea atajos interactivos con total libertad.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 8.dp),
            lineHeight = 22.sp
        )
    }
}

@Composable
fun FeaturesStep() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Acciones Potentes",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            FeatureItem(
                icon = Icons.Default.VolumeUp,
                title = "Texto a Voz y Audio",
                description = "Reproduce sonidos de alarma y haz que tu celular hable en voz alta con entonación natural."
            )
            FeatureItem(
                icon = Icons.Default.PhotoCamera,
                title = "Captura de Cámara en Segundo Plano",
                description = "Toma fotografías silenciosas sin levantar la interfaz de usuario, directo a tu galería local."
            )
            FeatureItem(
                icon = Icons.Default.TouchApp,
                title = "Toques y Deslizamientos Inteligentes",
                description = "Simula pulsaciones de botones y deslizamientos de pantalla basados en coordenadas exactas."
            )
            FeatureItem(
                icon = Icons.Default.ContentPaste,
                title = "Portapapeles Silencioso",
                description = "Copia y extrae información del portapapeles del sistema para integrarlo de forma dinámica."
            )
        }
    }
}

@Composable
fun PermissionsStep() {
    val context = LocalContext.current
    
    // Permission states
    var isCameraGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }
    
    var isNotificationGranted by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    var isAccessibilityActive by remember {
        mutableStateOf(ShortcutAccessibilityService.isServiceRunning())
    }

    var showAccessibilityDialog by remember { mutableStateOf(false) }

    // Launchers
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        isCameraGranted = granted
    }

    val notificationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        isNotificationGranted = granted
    }

    // Refresh accessibility state when returning to the app
    DisposableEffect(Unit) {
        isAccessibilityActive = ShortcutAccessibilityService.isServiceRunning()
        onDispose { }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Permisos Bajo tu Control",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Tú decides qué permisos otorgar. No te obligamos a activar nada: habilita solo las capacidades que necesites usar.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 18.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Notifications Permission Card
            PermissionCard(
                icon = Icons.Default.Notifications,
                title = "Notificaciones",
                description = "Requerido para avisarte sobre la ejecución y finalización de tus atajos.",
                isGranted = isNotificationGranted,
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        notificationLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            )

            // Camera Permission Card
            PermissionCard(
                icon = Icons.Default.PhotoCamera,
                title = "Cámara (Opcional)",
                description = "Solo es necesario si vas a utilizar el bloque de acción de captura en segundo plano.",
                isGranted = isCameraGranted,
                onClick = {
                    cameraLauncher.launch(android.Manifest.permission.CAMERA)
                }
            )

            // Accessibility Service Card
            PermissionCard(
                icon = Icons.Default.AccessibilityNew,
                title = "Servicio de Accesibilidad",
                description = "Permite a Kinetix simular toques físicos y gestos automáticos de forma local y segura.",
                isGranted = isAccessibilityActive,
                actionLabel = if (isAccessibilityActive) "Activo" else "Habilitar",
                onClick = {
                    if (!isAccessibilityActive) {
                        showAccessibilityDialog = true
                    }
                }
            )
        }
    }

    if (showAccessibilityDialog) {
        AlertDialog(
            onDismissRequest = { showAccessibilityDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.AccessibilityNew,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    "Servicio de Accesibilidad",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "Kinetix necesita ejecutarse de fondo como servicio de accesibilidad local para presionar botones, navegar hacia atrás, hacer scroll e interactuar con otras aplicaciones de forma automática.",
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                    
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.25f)
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.3f)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                "⚠️ ¿Bloqueado por Android? (Ajuste restringido)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.error
                            )
                            Text(
                                "Si Android bloquea el interruptor de accesibilidad por seguridad:\n" +
                                        "1. Toca 'Ir a Info de la App' abajo.\n" +
                                        "2. Pulsa los 3 puntos en la esquina superior derecha.\n" +
                                        "3. Selecciona 'Permitir ajustes restringidos'.\n" +
                                        "4. Regresa aquí y pulsa 'Activar Ajustes'.",
                                fontSize = 11.sp,
                                lineHeight = 15.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = {
                            try {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = android.net.Uri.fromParts("package", context.packageName, null)
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                // fallback
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Info de la App", fontSize = 11.sp)
                    }

                    Button(
                        onClick = {
                            showAccessibilityDialog = false
                            try {
                                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                // fallback
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Activar Ajustes", fontSize = 11.sp)
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showAccessibilityDialog = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar", textAlign = TextAlign.Center)
                }
            }
        )
    }
}

@Composable
fun FeatureItem(
    icon: ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun PermissionCard(
    icon: ImageVector,
    title: String,
    description: String,
    isGranted: Boolean,
    actionLabel: String = "Conceder",
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isGranted) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
            else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            1.dp,
            if (isGranted) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            else MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        if (isGranted) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isGranted) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 14.sp
                )
            }

            Button(
                onClick = onClick,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isGranted) MaterialTheme.colorScheme.secondaryContainer
                    else MaterialTheme.colorScheme.primary,
                    contentColor = if (isGranted) MaterialTheme.colorScheme.onSecondaryContainer
                    else MaterialTheme.colorScheme.onPrimary
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text(
                    text = if (isGranted) "Concedido" else actionLabel,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

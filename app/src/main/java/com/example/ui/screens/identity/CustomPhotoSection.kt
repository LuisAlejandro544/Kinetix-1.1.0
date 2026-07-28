package com.example.ui.screens.identity

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun CustomPhotoSection(
    customPhotoUri: String?,
    onCustomPhotoUriChange: (String?) -> Unit
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            onCustomPhotoUriChange(uri.toString())
        }
    }

    Column {
        HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f))
        Spacer(modifier = Modifier.height(16.dp))

        Text("Foto de Fondo Personalizada (Opcional)", fontSize = 13.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        if (customPhotoUri != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Async Image Preview
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.5.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                ) {
                    AsyncImage(
                        model = customPhotoUri,
                        contentDescription = "Vista previa de la foto del atajo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Button(
                        onClick = { photoPickerLauncher.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text("Cambiar Foto", fontSize = 12.sp)
                    }

                    TextButton(
                        onClick = { onCustomPhotoUriChange(null) },
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("Quitar Foto", fontSize = 12.sp)
                    }
                }
            }
        } else {
            Button(
                onClick = { photoPickerLauncher.launch("image/*") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.outlineVariant,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("select_photo_button")
            ) {
                Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Seleccionar Imagen de Galería", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

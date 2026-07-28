package com.example.ui.screens.inputs

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CleanTextFieldWithLink(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val hasPlaceholder = value.contains("{resultado}") || value.contains("\${input}")
    val cleanText = value.replace("{resultado}", "")
                         .replace("\${input}", "")

    Column(modifier = modifier) {
        OutlinedTextField(
            value = cleanText,
            onValueChange = { newTypedText ->
                if (hasPlaceholder) {
                    val joined = if (newTypedText.isEmpty()) "{resultado}" else "${newTypedText}{resultado}"
                    onValueChange(joined)
                } else {
                    onValueChange(newTypedText)
                }
            },
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = singleLine,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions
        )
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Link,
                contentDescription = null,
                tint = if (hasPlaceholder) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "Enlazar con el resultado del paso anterior",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 11.sp,
                color = if (hasPlaceholder) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = hasPlaceholder,
                onCheckedChange = { checked ->
                    if (checked) {
                        val joined = if (cleanText.isEmpty()) "{resultado}" else "${cleanText}{resultado}"
                        onValueChange(joined)
                    } else {
                        onValueChange(cleanText)
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                    uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.scale(0.8f)
            )
        }
    }
}

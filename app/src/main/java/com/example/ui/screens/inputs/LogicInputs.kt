package com.example.ui.screens.inputs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextInputFieldInput(
    params: Map<String, String>,
    onParamsChanged: (Map<String, String>) -> Unit
) {
    val prompt = params["prompt"] ?: ""
    val defaultValue = params["defaultValue"] ?: ""
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = prompt,
            onValueChange = { onParamsChanged(params + ("prompt" to it)) },
            label = { Text("Pregunta (Prompt) al usuario") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = defaultValue,
            onValueChange = { onParamsChanged(params + ("defaultValue" to it)) },
            label = { Text("Valor por defecto") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextTransformInput(
    params: Map<String, String>,
    onParamsChanged: (Map<String, String>) -> Unit
) {
    val selectedType = params["transformType"] ?: "UPPERCASE"
    val options = listOf(
        "UPPERCASE" to "MAYÚSCULAS",
        "lowercase" to "minúsculas",
        "Reverse" to "Reverso de texto",
        "Word Count" to "Contar palabras"
    )
    Column {
        Text("Método de transformación:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(4.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(options.size) { index ->
                val opt = options[index]
                val isSelected = selectedType == opt.first
                FilterChip(
                    selected = isSelected,
                    onClick = { onParamsChanged(params + ("transformType" to opt.first)) },
                    label = { Text(opt.second, fontSize = 11.sp) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MathOpInput(
    params: Map<String, String>,
    onParamsChanged: (Map<String, String>) -> Unit
) {
    val operation = params["operation"] ?: "Add"
    val operand = params["operand"] ?: "1"
    val mathOps = listOf(
        "Add" to "Sumar (+)",
        "Subtract" to "Restar (-)",
        "Multiply" to "Multiplicar (×)",
        "Divide" to "Dividir (÷)"
    )
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Operación matemática:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(mathOps.size) { index ->
                val op = mathOps[index]
                val isSelected = operation == op.first
                FilterChip(
                    selected = isSelected,
                    onClick = { onParamsChanged(params + ("operation" to op.first)) },
                    label = { Text(op.second, fontSize = 11.sp) }
                )
            }
        }
        OutlinedTextField(
            value = operand,
            onValueChange = { onParamsChanged(params + ("operand" to it)) },
            label = { Text("Operando (Número)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConditionalInput(
    params: Map<String, String>,
    onParamsChanged: (Map<String, String>) -> Unit
) {
    val value = params["value"] ?: "{resultado}"
    val operator = params["operator"] ?: "Equals"
    val compareValue = params["compareValue"] ?: ""
    val thenValue = params["thenValue"] ?: "Sí"
    val elseValue = params["elseValue"] ?: "No"

    val operators = listOf(
        "Equals" to "Igual",
        "NotEquals" to "Diferente",
        "Contains" to "Contiene",
        "GreaterThan" to "Mayor que ( > )",
        "LessThan" to "Menor que ( < )"
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        CleanTextFieldWithLink(
            value = value,
            onValueChange = { onParamsChanged(params + ("value" to it)) },
            label = "Valor a evaluar",
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Text("Operador de comparación:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(operators.size) { index ->
                val op = operators[index]
                val isSelected = operator == op.first
                FilterChip(
                    selected = isSelected,
                    onClick = { onParamsChanged(params + ("operator" to op.first)) },
                    label = { Text(op.second, fontSize = 11.sp) }
                )
            }
        }

        OutlinedTextField(
            value = compareValue,
            onValueChange = { onParamsChanged(params + ("compareValue" to it)) },
            label = { Text("Comparar con el valor") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = thenValue,
                onValueChange = { onParamsChanged(params + ("thenValue" to it)) },
                label = { Text("Si es VERDADERO") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                supportingText = { Text("Salida si cumple") }
            )
            OutlinedTextField(
                value = elseValue,
                onValueChange = { onParamsChanged(params + ("elseValue" to it)) },
                label = { Text("Si es FALSO") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                supportingText = { Text("Salida si no cumple") }
            )
        }
    }
}

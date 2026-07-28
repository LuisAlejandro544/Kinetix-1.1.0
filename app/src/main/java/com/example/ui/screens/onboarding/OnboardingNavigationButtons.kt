package com.example.ui.screens.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun OnboardingNavigationButtons(
    currentStep: Int,
    totalSteps: Int,
    onBack: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (currentStep > 0) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.height(50.dp)
            ) {
                Text("Atrás")
            }
        } else {
            Spacer(modifier = Modifier.width(1.dp))
        }

        Button(
            onClick = onNext,
            modifier = Modifier
                .weight(1f)
                .padding(start = if (currentStep > 0) 12.dp else 0.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = if (currentStep == totalSteps - 1) "Comenzar" else "Siguiente",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

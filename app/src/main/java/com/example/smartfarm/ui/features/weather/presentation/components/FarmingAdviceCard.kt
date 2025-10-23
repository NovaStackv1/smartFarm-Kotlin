package com.example.smartfarm.ui.features.weather.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartfarm.ui.features.weather.domain.models.AdviceType
import com.example.smartfarm.ui.features.weather.domain.models.FarmingAdvice

@Composable
fun FarmingAdviceCard(advice: FarmingAdvice) {
    val enterTransition = remember {
        slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(durationMillis = 300)
        ) + fadeIn(animationSpec = tween(durationMillis = 300))
    }

    AnimatedVisibility(
        visible = true,
        enter = enterTransition
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = when (advice.type) {
                    AdviceType.POSITIVE -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    AdviceType.WARNING -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                    AdviceType.INFO -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
                }
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = when (advice.type) {
                        AdviceType.POSITIVE -> Icons.Default.CheckCircle
                        AdviceType.WARNING -> Icons.Default.Warning
                        AdviceType.INFO -> Icons.Default.Lightbulb
                    },
                    contentDescription = null,
                    tint = when (advice.type) {
                        AdviceType.POSITIVE -> MaterialTheme.colorScheme.primary
                        AdviceType.WARNING -> MaterialTheme.colorScheme.error
                        AdviceType.INFO -> MaterialTheme.colorScheme.secondary
                    },
                    modifier = Modifier.size(24.dp)
                )

                Text(
                    text = advice.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
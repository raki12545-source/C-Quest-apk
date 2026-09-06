package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SimulationStatus
import com.example.ui.theme.*

@Composable
fun BentoHeroCard(
    title: String,
    subtitle: String,
    status: SimulationStatus,
    targetPointer: String,
    targetSize: String,
    stepFraction: String,
    loopInfo: Pair<String, Int>?,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "heroPulse")
    val dotAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dotAlpha"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(BentoPrimaryContainer)
            .border(1.dp, BentoBorderLight, RoundedCornerShape(24.dp))
            .padding(18.dp)
            .testTag("bento_hero_card")
    ) {
        // Subtle background watermark icon
        Icon(
            imageVector = Icons.Default.Memory,
            contentDescription = null,
            tint = BentoPrimaryDark,
            modifier = Modifier
                .size(110.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 18.dp, y = 18.dp)
                .alpha(0.08f)
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                when (status) {
                                    SimulationStatus.RUNNING -> SynSuccess.copy(alpha = dotAlpha)
                                    SimulationStatus.STEPPING -> BentoPrimaryDark.copy(alpha = dotAlpha)
                                    SimulationStatus.COMPLETED -> SynSuccess
                                    SimulationStatus.PANIC_ERROR -> SynError.copy(alpha = dotAlpha)
                                    else -> BentoPrimaryDark.copy(alpha = 0.5f)
                                }
                            )
                    )
                    Text(
                        text = when (status) {
                            SimulationStatus.RUNNING -> "EXECUTION ACTIVE"
                            SimulationStatus.STEPPING -> "STEPPING INSTRUCTION"
                            SimulationStatus.COMPLETED -> "SIMULATION COMPLETE"
                            SimulationStatus.PAUSED -> "SIMULATION PAUSED"
                            SimulationStatus.PANIC_ERROR -> "KERNEL EXCEPTION"
                            SimulationStatus.IDLE -> "KERNEL IDLE"
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = BentoPrimaryDark,
                        letterSpacing = 1.sp
                    )
                }

                Text(
                    text = title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = BentoPrimaryDark,
                    letterSpacing = (-0.5).sp,
                    lineHeight = 26.sp
                )

                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = BentoPrimaryDark.copy(alpha = 0.75f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Metric tags
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MetricChip(label = "ptr", value = targetPointer)
                MetricChip(label = "size", value = targetSize)
                MetricChip(label = "step", value = stepFraction)
                if (loopInfo != null) {
                    MetricChip(label = loopInfo.first, value = loopInfo.second.toString(), highlight = true)
                }
            }
        }
    }
}

@Composable
private fun MetricChip(
    label: String,
    value: String,
    highlight: Boolean = false
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .background(if (highlight) BentoPrimary else Color.White.copy(alpha = 0.65f))
            .border(
                width = 1.dp,
                color = if (highlight) BentoPrimaryDark else BentoPrimaryDark.copy(alpha = 0.15f),
                shape = RoundedCornerShape(100.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = "$label: $value",
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.SemiBold,
            color = if (highlight) Color.White else BentoPrimaryDark
        )
    }
}

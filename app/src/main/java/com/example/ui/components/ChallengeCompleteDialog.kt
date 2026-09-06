package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.Level
import com.example.ui.theme.*

@Composable
fun ChallengeCompleteDialog(
    level: Level,
    selectedOption: Int?,
    result: Boolean?,
    onSelectOption: (Int) -> Unit,
    onSubmit: () -> Unit,
    onNextLevel: () -> Unit,
    onDismiss: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "celebAnim")
    val starScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "starScale"
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = BentoBackground,
            border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorderLight),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .testTag("challenge_complete_dialog")
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header with icon
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .scale(starScale)
                        .clip(CircleShape)
                        .background(BentoPrimaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Celebration,
                        contentDescription = "Success",
                        tint = BentoPrimaryDark,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Text(
                    text = if (result == true) "Level Mastered!" else "Assessment: ${level.title}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BentoPrimaryDark,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = level.challengeQuestion,
                    fontSize = 13.sp,
                    color = BentoTextPrimary,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )

                // Code snippet hint box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(BentoConsoleBackground)
                        .padding(10.dp)
                ) {
                    Text(
                        text = formatCSyntax(level.cSnippetSnippet),
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        lineHeight = 15.sp
                    )
                }

                // Options
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    level.challengeOptions.forEachIndexed { index, option ->
                        val isSelected = selectedOption == index
                        val isCorrectOption = index == level.correctOptionIndex
                        val optBg = when {
                            result == true && isSelected -> Color(0xFFDCFCE7)
                            result == false && isSelected -> Color(0xFFFEE2E2)
                            isSelected -> BentoPrimaryContainer
                            else -> BentoSurfaceVariant
                        }
                        val optBorder = when {
                            result == true && isSelected -> SynSuccess
                            result == false && isSelected -> SynError
                            isSelected -> BentoPrimary
                            else -> BentoBorderOutline
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(optBg)
                                .border(1.dp, optBorder, RoundedCornerShape(12.dp))
                                .clickable(enabled = result == null) { onSelectOption(index) }
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                                .testTag("challenge_opt_$index"),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = option,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                color = BentoTextPrimary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.weight(1f)
                            )
                            if (result != null && isSelected) {
                                Icon(
                                    imageVector = if (result == true) Icons.Default.CheckCircle else Icons.Default.Error,
                                    contentDescription = null,
                                    tint = if (result == true) SynSuccess else SynError,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                // Feedback
                if (result != null) {
                    Text(
                        text = if (result) "Superb! Next Level unlocked (+100 Credits)" else level.challengeHint,
                        fontSize = 11.5.sp,
                        color = if (result) Color(0xFF15803D) else Color(0xFFB91C1C),
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(100.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorderOutline)
                    ) {
                        Text("Review Code", fontSize = 12.sp, color = BentoTextSecondary)
                    }

                    Button(
                        onClick = {
                            if (result == null) {
                                onSubmit()
                            } else if (result == true) {
                                onNextLevel()
                            } else {
                                onDismiss()
                            }
                        },
                        enabled = selectedOption != null,
                        modifier = Modifier.weight(1f).height(44.dp).testTag("challenge_confirm_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BentoPrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(100.dp)
                    ) {
                        Text(
                            text = if (result == null) "Submit Answer" else if (result == true) "Next Level" else "Retry",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

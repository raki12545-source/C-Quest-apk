package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
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
import androidx.compose.ui.window.Dialog
import com.example.model.InGameEvent
import com.example.ui.theme.*

@Composable
fun EventSolveDialog(
    event: InGameEvent,
    selectedOption: Int?,
    result: Boolean?,
    onSelectOption: (Int) -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = BentoBackground,
            border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorderLight),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .testTag("event_solve_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(BentoPrimaryContainer)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = event.urgency.uppercase(),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = BentoPrimaryDark,
                                letterSpacing = 0.5.sp
                            )
                        }
                        Text(
                            text = event.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = BentoTextPrimary,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(BentoSurfaceVariant)
                            .testTag("close_event_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = BentoTextPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // Mission Brief
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(BentoSurfaceVariant)
                        .border(1.dp, BentoBorderOutline, RoundedCornerShape(16.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = event.missionBrief,
                        fontSize = 12.sp,
                        color = BentoTextPrimary,
                        lineHeight = 17.sp
                    )
                }

                // C Code snippet box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(BentoConsoleBackground)
                        .border(1.dp, Color(0xFF2B2930), RoundedCornerShape(16.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = formatCSyntax(event.codeSnippet),
                        fontSize = 11.5.sp,
                        fontFamily = FontFamily.Monospace,
                        lineHeight = 16.sp
                    )
                }

                // Question
                Text(
                    text = event.question,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BentoPrimaryDark
                )

                // Choices
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    event.choices.forEachIndexed { index, choice ->
                        val isSelected = selectedOption == index
                        val isCorrectOption = index == event.correctIndex
                        val choiceBg = when {
                            result == true && isSelected -> Color(0xFFDCFCE7)
                            result == false && isSelected -> Color(0xFFFEE2E2)
                            isSelected -> BentoPrimaryContainer
                            else -> BentoSurfaceVariant
                        }
                        val choiceBorder = when {
                            result == true && isSelected -> SynSuccess
                            result == false && isSelected -> SynError
                            isSelected -> BentoPrimary
                            else -> BentoBorderOutline
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(choiceBg)
                                .border(1.dp, choiceBorder, RoundedCornerShape(12.dp))
                                .clickable(enabled = result == null) { onSelectOption(index) }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                                .testTag("event_choice_$index"),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = choice,
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
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                // Feedback
                if (result != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (result) Color(0xFFECFDF5) else Color(0xFFFEF2F2))
                            .border(1.dp, if (result) SynSuccess else SynError, RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = if (result) "SUCCESS: ${event.explanation} (+${event.rewardPoints} Core Credits)" else "INCORRECT: Check the logic and try again.",
                            fontSize = 11.5.sp,
                            color = if (result) Color(0xFF065F46) else Color(0xFF991B1B),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Action Button
                Button(
                    onClick = {
                        if (result == null) {
                            onSubmit()
                        } else {
                            onDismiss()
                        }
                    },
                    enabled = selectedOption != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("event_submit_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BentoPrimary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(100.dp)
                ) {
                    Text(
                        text = if (result == null) "Verify & Deploy C Patch" else "Continue Simulation",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

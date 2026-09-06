package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
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
import com.example.data.LevelRepository
import com.example.model.Level
import com.example.model.Tier
import com.example.ui.theme.*

@Composable
fun LevelSelectDialog(
    currentLevelId: Int,
    unlockedLevelIds: Set<Int>,
    completedLevelIds: Set<Int>,
    onSelectLevel: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = BentoBackground,
            border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorderLight),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .testTag("level_select_dialog")
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Level Progression",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = BentoPrimaryDark
                        )
                        Text(
                            text = "${completedLevelIds.size}/${LevelRepository.levels.size} Levels Mastered",
                            fontSize = 12.sp,
                            color = BentoTextSecondary
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(BentoSurfaceVariant)
                            .testTag("close_levels_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = BentoTextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Tier.values().forEach { tier ->
                        val tierLevels = LevelRepository.levels.filter { it.tier == tier }
                        if (tierLevels.isNotEmpty()) {
                            item {
                                Text(
                                    text = "${tier.label.uppercase()} CURRICULUM",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = BentoPrimary,
                                    letterSpacing = 1.sp,
                                    modifier = Modifier.padding(top = 6.dp, bottom = 2.dp)
                                )
                            }
                            items(tierLevels) { level ->
                                val isUnlocked = unlockedLevelIds.contains(level.id)
                                val isCompleted = completedLevelIds.contains(level.id)
                                val isCurrent = level.id == currentLevelId

                                LevelItemCard(
                                    level = level,
                                    isUnlocked = isUnlocked,
                                    isCompleted = isCompleted,
                                    isCurrent = isCurrent,
                                    onClick = {
                                        if (isUnlocked) {
                                            onSelectLevel(level.id)
                                            onDismiss()
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LevelItemCard(
    level: Level,
    isUnlocked: Boolean,
    isCompleted: Boolean,
    isCurrent: Boolean,
    onClick: () -> Unit
) {
    val bgColor = when {
        isCurrent -> BentoPrimaryContainer
        isUnlocked -> BentoSurfaceVariant
        else -> BentoSurfaceAlt.copy(alpha = 0.6f)
    }

    val borderColor = when {
        isCurrent -> BentoPrimary
        isCompleted -> SynSuccess
        isUnlocked -> BentoBorderOutline
        else -> Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable(enabled = isUnlocked, onClick = onClick)
            .padding(14.dp)
            .testTag("level_card_${level.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isCompleted -> SynSuccess
                        isCurrent -> BentoPrimary
                        isUnlocked -> BentoSecondary
                        else -> Color(0xFF9E9AA4)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            when {
                isCompleted -> Icon(Icons.Default.CheckCircle, contentDescription = "Completed", tint = Color.White, modifier = Modifier.size(20.dp))
                isUnlocked -> Text(text = "${level.id}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                else -> Icon(Icons.Default.Lock, contentDescription = "Locked", tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = level.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isUnlocked) BentoTextPrimary else BentoTextSecondary
                )
                if (isCurrent) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(BentoPrimary)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("ACTIVE", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }

            Text(
                text = level.subtitle,
                fontSize = 11.sp,
                color = BentoTextSecondary,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        if (isUnlocked) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Start",
                tint = if (isCurrent) BentoPrimaryDark else BentoTextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

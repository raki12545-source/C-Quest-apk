package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Achievement
import com.example.model.BadgeTier

@Composable
fun BadgeUnlockBanner(
    achievement: Achievement?,
    onDismiss: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = achievement != null,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
        modifier = modifier
    ) {
        if (achievement == null) return@AnimatedVisibility

        val tierColor = Color(achievement.badgeTier.borderColorHex)
        val containerColor = Color(achievement.badgeTier.containerColorHex)
        val contentColor = Color(achievement.badgeTier.contentColorHex)

        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
        val glowAlpha by infiniteTransition.animateFloat(
            initialValue = 0.4f,
            targetValue = 0.9f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "glow"
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .shadow(12.dp, RoundedCornerShape(16.dp))
                .border(2.dp, tierColor.copy(alpha = glowAlpha), RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .clickable { onClick() }
                .testTag("badge_unlock_banner"),
            color = Color(0xFF1E1B4B) // Rich Midnight Obsidian
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Shiny Badge Icon Container
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(tierColor, containerColor)
                            )
                        )
                        .border(1.5.dp, Color.White.copy(alpha = 0.8f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (achievement.iconKey) {
                            "pointer_pro" -> Icons.Default.NearMe
                            "memory_master" -> Icons.Default.Memory
                            "compile" -> Icons.Default.PlayArrow
                            "stack" -> Icons.Default.Layers
                            "loop" -> Icons.Default.Sync
                            "bug" -> Icons.Default.BugReport
                            "struct" -> Icons.Default.AccountTree
                            "speed" -> Icons.Default.Speed
                            "codex" -> Icons.Default.MenuBook
                            "crown" -> Icons.Default.EmojiEvents
                            else -> Icons.Default.WorkspacePremium
                        },
                        contentDescription = achievement.title,
                        tint = contentColor,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Text details
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = tierColor.copy(alpha = 0.25f)
                        ) {
                            Text(
                                text = "BADGE UNLOCKED",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = tierColor,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                letterSpacing = 1.sp
                            )
                        }

                        Text(
                            text = "+${achievement.rewardCredits} XP",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4ADE80),
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = achievement.title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = achievement.subtitle,
                        fontSize = 12.sp,
                        color = Color(0xFFCBD5E1),
                        maxLines = 1
                    )
                }

                // Close / dismiss action
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("dismiss_badge_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

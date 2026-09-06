package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MemoryCell
import com.example.model.MemoryType
import com.example.ui.theme.*

@Composable
fun MemoryCellView(
    cell: MemoryCell,
    isPointerTarget: Boolean = false,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulseTransition")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (cell.isHighlighted) 1.03f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            cell.isCorrupted -> SynError
            cell.isFreed -> SynSuccess
            cell.isHighlighted -> BentoPrimary
            isPointerTarget -> SynPointer
            else -> BentoBorderOutline
        },
        label = "cellBorder"
    )

    val bgColor by animateColorAsState(
        targetValue = when {
            cell.isFreed -> Color(0xFFE8F5E9)
            cell.isCorrupted -> Color(0xFFFFEBEE)
            cell.isHighlighted -> BentoPrimaryContainer.copy(alpha = 0.6f)
            cell.memoryType == MemoryType.HEAP -> Color(0xFFFFF8E1)
            else -> Color.White
        },
        label = "cellBg"
    )

    Box(
        modifier = modifier
            .scale(if (cell.isHighlighted) pulseScale else 1f)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(
                width = if (cell.isHighlighted || isPointerTarget) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .testTag("memory_cell_${cell.name}")
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = cell.addressHex,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    color = BentoTextSecondary,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = if (cell.memoryType == MemoryType.HEAP) "HEAP [${cell.sizeBytes}B]" else "STACK [${cell.sizeBytes}B]",
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace,
                    color = if (cell.memoryType == MemoryType.HEAP) Color(0xFFB45309) else BentoPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = cell.typeName,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = SynType,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = cell.name,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        color = BentoTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = cell.value,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = when {
                        cell.isFreed -> SynSuccess
                        cell.isCorrupted -> SynError
                        cell.pointsToAddress != null -> SynPointer
                        else -> BentoPrimaryDark
                    },
                    fontWeight = FontWeight.Bold
                )
            }

            if (cell.pointsToAddress != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Pointer Wire",
                        tint = SynPointer,
                        modifier = Modifier.size(10.dp)
                    )
                    Text(
                        text = "wires to ${cell.pointsToAddress}",
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        color = SynPointer,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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
import com.example.model.StackFrame
import com.example.ui.theme.*

@Composable
fun TheStackCard(
    frames: List<StackFrame>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(BentoSurfaceVariant)
            .border(1.dp, BentoBorderOutline, RoundedCornerShape(24.dp))
            .padding(14.dp)
            .testTag("the_stack_card")
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "THE STACK",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = BentoTextSecondary,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "${frames.size} frame${if (frames.size > 1) "s" else ""}",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    color = BentoPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Stack grows upward or displays LIFO from top to bottom
                frames.asReversed().forEach { frame ->
                    AnimatedVisibility(
                        visible = true,
                        enter = slideInVertically() + fadeIn(),
                        exit = slideOutVertically() + fadeOut()
                    ) {
                        StackFrameRow(frame = frame)
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "LIFO Call Stack",
                    fontSize = 10.sp,
                    color = BentoTextSecondary,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
                Text(
                    text = "Top: ${frames.lastOrNull()?.functionName ?: "None"}",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    color = BentoPrimaryDark,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun StackFrameRow(frame: StackFrame) {
    val bgColor = if (frame.isActive) BentoPrimary else BentoSurfaceAlt
    val textColor = if (frame.isActive) Color.White else BentoTextPrimary
    val borderColor = if (frame.isActive) BentoPrimaryDark else BentoPrimary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(28.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(
                width = 1.dp,
                color = if (frame.isActive) BentoPrimaryDark else BentoBorderOutline,
                shape = RoundedCornerShape(8.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left accent stripe
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .background(borderColor)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = frame.functionName,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.weight(1f)
        )
        if (frame.parameters.isNotEmpty()) {
            Text(
                text = frame.parameters.joinToString(", "),
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace,
                color = if (frame.isActive) BentoPrimaryContainer else BentoTextSecondary,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

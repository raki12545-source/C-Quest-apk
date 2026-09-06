package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun StdoutBentoCard(
    stdoutText: String,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val lines = stdoutText.lines()

    LaunchedEffect(lines.size) {
        if (lines.isNotEmpty()) {
            listState.animateScrollToItem(lines.size - 1)
        }
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(BentoConsoleBackground)
            .border(1.dp, Color(0xFF2E2C33), RoundedCornerShape(20.dp))
            .padding(12.dp)
            .testTag("stdout_bento_card")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "STDOUT / GCC OUTPUT",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.5f),
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "fd: 1",
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    color = BentoBorderLight
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(lines) { line ->
                    val color = when {
                        line.startsWith("gcc") -> Color(0xFF94A3B8)
                        line.contains("[Process") || line.contains("[Build") || line.contains("Clean") -> SynSuccess
                        line.contains("Error") || line.contains("Panic") || line.contains("Fault") -> SynError
                        line.startsWith("->") || line.startsWith("<-") -> Color(0xFF38BDF8)
                        else -> Color(0xFFE2E8F0)
                    }
                    Text(
                        text = line,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = color,
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }
}

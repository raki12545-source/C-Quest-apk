package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun EventBentoCard(
    eventTitle: String,
    eventSubtitle: String,
    isHazard: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "hazardPulse")
    val hazardScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isHazard) 1.05f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hazardScale"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(if (isHazard) Color(0xFFFFEBEE) else BentoSurfaceAlt)
            .border(
                width = if (isHazard) 2.dp else 1.dp,
                color = if (isHazard) SynError else BentoBorderOutline,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable(onClick = onClick)
            .padding(12.dp)
            .testTag("event_bento_card")
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .scale(hazardScale)
                    .clip(CircleShape)
                    .background(if (isHazard) SynError else BentoSecondary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isHazard) Icons.Default.WarningAmber else Icons.Default.EventNote,
                    contentDescription = "Event Icon",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = eventTitle,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isHazard) SynError else BentoTextPrimary,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = eventSubtitle,
                fontSize = 10.sp,
                color = if (isHazard) Color(0xFFB91C1C) else BentoTextSecondary,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

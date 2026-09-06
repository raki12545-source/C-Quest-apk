package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = BentoBorderLight,
    onPrimary = BentoPrimaryDark,
    primaryContainer = BentoPrimary,
    onPrimaryContainer = BentoPrimaryContainer,
    secondary = BentoSecondaryContainer,
    onSecondary = BentoSecondary,
    background = BentoConsoleBackground,
    surface = Color(0xFF25232A),
    surfaceVariant = Color(0xFF332F38),
    onBackground = Color(0xFFE6E1E5),
    onSurface = Color(0xFFE6E1E5),
    outline = BentoBorderOutline,
    outlineVariant = Color(0xFF49454F),
  )

private val LightColorScheme =
  lightColorScheme(
    primary = BentoPrimary,
    onPrimary = Color.White,
    primaryContainer = BentoPrimaryContainer,
    onPrimaryContainer = BentoOnPrimaryContainer,
    secondary = BentoSecondary,
    onSecondary = Color.White,
    secondaryContainer = BentoSecondaryContainer,
    background = BentoBackground,
    surface = BentoBackground,
    surfaceVariant = BentoSurfaceVariant,
    onBackground = BentoTextPrimary,
    onSurface = BentoTextPrimary,
    onSurfaceVariant = BentoTextSecondary,
    outline = BentoBorderOutline,
    outlineVariant = BentoBorderLight,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Preserve crafted Bento Grid palette
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

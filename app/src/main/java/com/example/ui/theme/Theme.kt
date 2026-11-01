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

private val AspectLabDarkColorScheme = darkColorScheme(
  primary = CyberCyan,
  onPrimary = Color(0xFF00363F),
  primaryContainer = Color(0xFF004E5B),
  onPrimaryContainer = Color(0xFFB3F5FF),
  secondary = NeonAmber,
  onSecondary = Color(0xFF452B00),
  secondaryContainer = Color(0xFF623F00),
  onSecondaryContainer = Color(0xFFFFDF9E),
  tertiary = MintGreen,
  onTertiary = Color(0xFF003919),
  tertiaryContainer = Color(0xFF005226),
  onTertiaryContainer = Color(0xFF70FF9D),
  background = DarkCanvasBg,
  onBackground = TextPrimary,
  surface = DarkSurface,
  onSurface = TextPrimary,
  surfaceVariant = DarkSurfaceVariant,
  onSurfaceVariant = TextSecondary,
  outline = DarkSurfaceBorder,
  outlineVariant = Color(0xFF334155),
  error = NeonCoral,
  onError = Color.White
)

private val AspectLabLightColorScheme = lightColorScheme(
  primary = Color(0xFF006877),
  onPrimary = Color.White,
  primaryContainer = Color(0xFFA1EFFF),
  onPrimaryContainer = Color(0xFF001F25),
  secondary = Color(0xFF7C5800),
  onSecondary = Color.White,
  secondaryContainer = Color(0xFFFFDEA4),
  onSecondaryContainer = Color(0xFF271900),
  tertiary = Color(0xFF006D35),
  onTertiary = Color.White,
  background = Color(0xFFF8FAFC),
  onBackground = Color(0xFF0F172A),
  surface = Color(0xFFFFFFFF),
  onSurface = Color(0xFF0F172A),
  surfaceVariant = Color(0xFFE2E8F0),
  onSurfaceVariant = Color(0xFF475569),
  outline = Color(0xFFCBD5E1)
)

@Composable
fun AspectLabTheme(
  darkTheme: Boolean = true, // Default to professional dark workstation for video editing
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> AspectLabDarkColorScheme
    else -> AspectLabLightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

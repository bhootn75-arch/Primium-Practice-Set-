package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
  primary = Blue600,
  onPrimary = CardLight,
  primaryContainer = Blue100,
  onPrimaryContainer = Navy900,
  secondary = Amber500,
  onSecondary = Navy900,
  secondaryContainer = Amber100,
  onSecondaryContainer = Navy900,
  tertiary = Green600,
  onTertiary = CardLight,
  tertiaryContainer = Green100,
  onTertiaryContainer = Green600,
  background = SurfaceLight,
  onBackground = TextPrimary,
  surface = CardLight,
  onSurface = TextPrimary,
  surfaceVariant = Blue50,
  onSurfaceVariant = TextSecondary,
  outline = BorderLight,
  error = Red600,
  onError = CardLight,
  errorContainer = Red100,
  onErrorContainer = Red600
)

private val DarkColorScheme = darkColorScheme(
  primary = Blue500,
  onPrimary = Navy900,
  primaryContainer = Navy700,
  onPrimaryContainer = Blue100,
  secondary = Amber500,
  onSecondary = Navy900,
  secondaryContainer = Amber700,
  onSecondaryContainer = CardLight,
  tertiary = Green600,
  onTertiary = CardLight,
  tertiaryContainer = Green100,
  onTertiaryContainer = Green600,
  background = SurfaceDark,
  onBackground = TextPrimaryDark,
  surface = CardDark,
  onSurface = TextPrimaryDark,
  surfaceVariant = Navy800,
  onSurfaceVariant = TextSecondaryDark,
  outline = BorderDark,
  error = Red600,
  onError = CardLight,
  errorContainer = Red100,
  onErrorContainer = Red600
)

@Composable
fun PracticeSetTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as Activity).window
      window.statusBarColor = colorScheme.background.toArgb()
      WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
    }
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

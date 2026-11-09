package com.gymstudio.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = AccentBlueTurquoise,
    onPrimary = Color.White,
    primaryContainer = AccentContainerLight,
    onPrimaryContainer = InkOnLight,
    secondary = AccentBlueTurquoise,
    onSecondary = Color.White,
    background = CreamBackground,
    onBackground = InkOnLight,
    surface = CreamSurface,
    onSurface = InkOnLight,
    surfaceVariant = CreamSurfaceVariant,
    onSurfaceVariant = MutedOnLight,
    outline = CreamOutline,
)

private val DarkColors = darkColorScheme(
    primary = AccentDark,
    onPrimary = Color(0xFF062A2E),
    primaryContainer = AccentContainerDark,
    onPrimaryContainer = InkOnDark,
    secondary = AccentDark,
    onSecondary = Color(0xFF062A2E),
    background = DarkBackground,
    onBackground = InkOnDark,
    surface = DarkSurface,
    onSurface = InkOnDark,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = MutedOnDark,
    outline = DarkOutline,
)

@Composable
fun GymStudioTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = GymTypography,
        content = content,
    )
}

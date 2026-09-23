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

private val DarkColorScheme = darkColorScheme(
    primary = MondalPrimaryDark,
    onPrimary = MondalDarkGreen,
    primaryContainer = MondalAccentGreen,
    onPrimaryContainer = Color.White,
    secondary = MondalGold,
    onSecondary = Color.Black,
    background = MondalBgDark,
    onBackground = Color.White,
    surface = MondalSurfaceDark,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF223531),
    onSurfaceVariant = Color(0xFFC0D2CC),
    error = Color(0xFFFFB4AB)
)

private val LightColorScheme = lightColorScheme(
    primary = MondalGreen,
    onPrimary = Color.White,
    primaryContainer = MondalLightGreen,
    onPrimaryContainer = MondalDarkGreen,
    secondary = MondalAccentGreen,
    onSecondary = Color.White,
    tertiary = MondalGold,
    onTertiary = Color.Black,
    background = MondalBgLight,
    onBackground = MondalTextPrimary,
    surface = MondalSurfaceLight,
    onSurface = MondalTextPrimary,
    surfaceVariant = Color(0xFFE9F3EE),
    onSurfaceVariant = MondalTextSecondary,
    error = MondalError,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep brand forest green as primary identity
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

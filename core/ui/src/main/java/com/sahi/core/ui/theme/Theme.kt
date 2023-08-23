package com.sahi.core.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

private val lightColorScheme = lightColorScheme(
    primary = com.sahi.core.ui.theme.md_theme_light_primary,
    onPrimary = com.sahi.core.ui.theme.md_theme_light_onPrimary,
    primaryContainer = com.sahi.core.ui.theme.md_theme_light_primaryContainer,
    onPrimaryContainer = com.sahi.core.ui.theme.md_theme_light_onPrimaryContainer,
    secondary = com.sahi.core.ui.theme.md_theme_light_secondary,
    onSecondary = com.sahi.core.ui.theme.md_theme_light_onSecondary,
    secondaryContainer = com.sahi.core.ui.theme.md_theme_light_secondaryContainer,
    onSecondaryContainer = com.sahi.core.ui.theme.md_theme_light_onSecondaryContainer,
    tertiary = com.sahi.core.ui.theme.md_theme_light_tertiary,
    onTertiary = com.sahi.core.ui.theme.md_theme_light_onTertiary,
    tertiaryContainer = com.sahi.core.ui.theme.md_theme_light_tertiaryContainer,
    onTertiaryContainer = com.sahi.core.ui.theme.md_theme_light_onTertiaryContainer,
    error = com.sahi.core.ui.theme.md_theme_light_error,
    errorContainer = com.sahi.core.ui.theme.md_theme_light_errorContainer,
    onError = com.sahi.core.ui.theme.md_theme_light_onError,
    onErrorContainer = com.sahi.core.ui.theme.md_theme_light_onErrorContainer,
    background = com.sahi.core.ui.theme.md_theme_light_background,
    onBackground = com.sahi.core.ui.theme.md_theme_light_onBackground,
    surface = com.sahi.core.ui.theme.md_theme_light_surface,
    onSurface = com.sahi.core.ui.theme.md_theme_light_onSurface,
    surfaceVariant = com.sahi.core.ui.theme.md_theme_light_surfaceVariant,
    onSurfaceVariant = com.sahi.core.ui.theme.md_theme_light_onSurfaceVariant,
    outline = com.sahi.core.ui.theme.md_theme_light_outline,
    inverseOnSurface = com.sahi.core.ui.theme.md_theme_light_inverseOnSurface,
    inverseSurface = com.sahi.core.ui.theme.md_theme_light_inverseSurface,
    inversePrimary = com.sahi.core.ui.theme.md_theme_light_inversePrimary,
    surfaceTint = com.sahi.core.ui.theme.md_theme_light_surfaceTint
)


private val darkColorScheme = darkColorScheme(
    primary = com.sahi.core.ui.theme.md_theme_dark_primary,
    onPrimary = com.sahi.core.ui.theme.md_theme_dark_onPrimary,
    primaryContainer = com.sahi.core.ui.theme.md_theme_dark_primaryContainer,
    onPrimaryContainer = com.sahi.core.ui.theme.md_theme_dark_onPrimaryContainer,
    secondary = com.sahi.core.ui.theme.md_theme_dark_secondary,
    onSecondary = com.sahi.core.ui.theme.md_theme_dark_onSecondary,
    secondaryContainer = com.sahi.core.ui.theme.md_theme_dark_secondaryContainer,
    onSecondaryContainer = com.sahi.core.ui.theme.md_theme_dark_onSecondaryContainer,
    tertiary = com.sahi.core.ui.theme.md_theme_dark_tertiary,
    onTertiary = com.sahi.core.ui.theme.md_theme_dark_onTertiary,
    tertiaryContainer = com.sahi.core.ui.theme.md_theme_dark_tertiaryContainer,
    onTertiaryContainer = com.sahi.core.ui.theme.md_theme_dark_onTertiaryContainer,
    error = com.sahi.core.ui.theme.md_theme_dark_error,
    errorContainer = com.sahi.core.ui.theme.md_theme_dark_errorContainer,
    onError = com.sahi.core.ui.theme.md_theme_dark_onError,
    onErrorContainer = com.sahi.core.ui.theme.md_theme_dark_onErrorContainer,
    background = com.sahi.core.ui.theme.md_theme_dark_background,
    onBackground = com.sahi.core.ui.theme.md_theme_dark_onBackground,
    surface = com.sahi.core.ui.theme.md_theme_dark_surface,
    onSurface = com.sahi.core.ui.theme.md_theme_dark_onSurface,
    surfaceVariant = com.sahi.core.ui.theme.md_theme_dark_surfaceVariant,
    onSurfaceVariant = com.sahi.core.ui.theme.md_theme_dark_onSurfaceVariant,
    outline = com.sahi.core.ui.theme.md_theme_dark_outline,
    inverseOnSurface = com.sahi.core.ui.theme.md_theme_dark_inverseOnSurface,
    inverseSurface = com.sahi.core.ui.theme.md_theme_dark_inverseSurface,
    inversePrimary = com.sahi.core.ui.theme.md_theme_dark_inversePrimary,
    surfaceTint = com.sahi.core.ui.theme.md_theme_dark_surfaceTint,
)

@Composable
fun ElingNoteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }
    val view = LocalView.current
    val window = (view.context as Activity).window
    if (!view.isInEditMode) {
        val currentWindow = (view.context as? Activity)?.window
            ?: throw Exception("Not in an activity - unable to get Window reference")

        SideEffect {
            window.statusBarColor = colorScheme.surfaceColorAtElevation(3.dp).toArgb()
            window.navigationBarColor = colorScheme.surfaceColorAtElevation(3.dp).toArgb()
            WindowCompat.getInsetsController(currentWindow, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(currentWindow, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
package com.kssidll.zapierdalo.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.platform.LocalContext
import com.kssidll.zapierdalo.ui.theme.schema.DarkColorScheme
import com.kssidll.zapierdalo.ui.theme.schema.LightColorScheme

const val disabledAlpha = 0.38f
const val optionalAlpha = 0.60f
val colorSpace = ColorSpaces.Srgb

/**
 * @return Color scheme to use
 * @param darkTheme Whether the color scheme should be a dark theme one
 * @param dynamicColor Whether to use dynamic color to build the color scheme
 */
@Composable
fun getColorScheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
): ColorScheme {
    return when {
        // dynamic color is available since API 31
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        // dark theme toggle option is available since API 29, so we default to it on lower API version
        darkTheme || Build.VERSION.SDK_INT < Build.VERSION_CODES.Q -> DarkColorScheme

        else -> LightColorScheme
    }
}

/**
 * @return Whether the app is considered to be in dark theme
 */
@Composable
fun isAppInDarkTheme(): Boolean {
    return isSystemInDarkTheme()
}

/**
 * @return Whether the app should use dynamic color to build the color scheme
 */
@Composable
fun isAppInDynamicColor(): Boolean {
    return true
}

/**
 * Default application theme
 * @param content Content to provide the theme to
 */
@Composable
fun ZapierdaloTheme(
    content: @Composable () -> Unit
) {
    val darkTheme = isAppInDarkTheme()
    val colorScheme = getColorScheme(
        darkTheme = darkTheme,
        dynamicColor = isAppInDynamicColor(),
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
    ) {
        content()
    }
}
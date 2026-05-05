package com.example.arogyaNidhi.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val ArogyaGreen = Color(0xFF1B7A4B)
val ArogyaGreenLight = Color(0xFF4CAF50)
val ArogyaGreenDark = Color(0xFF0D5C35)
val ArogyaTeal = Color(0xFF00897B)
val ArogyaGold = Color(0xFFF9A825)
val SurfaceWhite = Color(0xFFF8FFF9)
val BackgroundGreen = Color(0xFFE8F5E9)
val TextDark = Color(0xFF1A2B1F)
val TextMedium = Color(0xFF3D5A45)
val TextLight = Color(0xFF6B8F72)
val ErrorRed = Color(0xFFB71C1C)
val CardWhite = Color(0xFFFFFFFF)

private val ArogyaColorScheme = lightColorScheme(
    primary = ArogyaGreen,
    onPrimary = Color.White,
    primaryContainer = BackgroundGreen,
    onPrimaryContainer = ArogyaGreenDark,
    secondary = ArogyaTeal,
    onSecondary = Color.White,
    background = SurfaceWhite,
    onBackground = TextDark,
    surface = CardWhite,
    onSurface = TextDark,
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun ArogyaNidhiTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ArogyaColorScheme,
        typography = ArogyaTypography,
        content = content
    )
}
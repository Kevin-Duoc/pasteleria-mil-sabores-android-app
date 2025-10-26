package com.example.pasteleriamilsabores_grupo9.ui.theme

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
import androidx.compose.ui.graphics.Color

// --- TU NUEVA PALETA DE MODO CLARO ---
private val LightColorScheme = lightColorScheme(
    primary = Chocolate,            // Botones principales (Login)
    onPrimary = Blanco,             // Texto sobre botones principales
    secondary = RosaSuave,          // Botones secundarios (Ver productos)
    onSecondary = MarronOscuro,     // Texto sobre botones secundarios
    background = CremaPastel,       // Fondo de la app
    onBackground = MarronOscuro,    // Texto sobre el fondo
    surface = CremaPastel,          // Color de "superficies" (Cards, NavBars)
    onSurface = MarronOscuro,       // Texto sobre las superficies
    onSurfaceVariant = GrisClaro    // Texto secundario/descripciones
)

// --- TU NUEVA PALETA DE MODO OSCURO ---
private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkBackground,
    secondary = DarkSecondary,
    onSecondary = Color(0xFF4C2A2D),
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkBackground,
    onSurface = DarkOnBackground,
    onSurfaceVariant = DarkOnSurfaceVariant
)

@Composable
fun PasteleriaMilSabores_Grupo9Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb() // Color de la barra de estado
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // 'Typography' viene de Type.kt
        content = content
    )
}
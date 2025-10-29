package com.example.pasteleriamilsabores_grupo9.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.pasteleriamilsabores_grupo9.R

// Fuente Lobster (igual que antes)
val lobsterFamily = FontFamily(
    Font(R.font.lobster_regular, FontWeight.Normal)
)

// Tipografía actualizada
val Typography = Typography(
    // Estilo para texto normal (igual que antes)
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    // Estilo para el título principal (Lobster, igual que antes)
    headlineLarge = TextStyle(
        fontFamily = lobsterFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp
    ),
    // Estilo para el título en la barra superior (Lobster, igual que antes)
    titleLarge = TextStyle(
        fontFamily = lobsterFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),

    // --- 👇 NUEVO ESTILO AÑADIDO/MODIFICADO 👇 ---
    // Estilo para títulos secundarios (como "Productos") y texto destacado (como "Total:")
    // Usa la fuente NORMAL pero en NEGRITA y un tamaño mediano.
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default, // Fuente Normal
        fontWeight = FontWeight.Bold,   // Negrita
        fontSize = 20.sp, // Tamaño mediano (ajusta si prefieres otro)
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    // --- 👆 FIN DE NUEVO ESTILO 👆 ---

    // (Podríamos definir más estilos como bodyMedium, labelSmall, etc. si los necesitáramos)
)
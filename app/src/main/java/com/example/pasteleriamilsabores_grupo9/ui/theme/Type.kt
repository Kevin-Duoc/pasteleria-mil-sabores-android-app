package com.example.pasteleriamilsabores_grupo9.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.pasteleriamilsabores_grupo9.R // <-- 1. IMPORTANTE, AÑADIR ESTO

// 2. Definimos la nueva familia de fuentes "Lobster"
//    R.font.lobster_regular es el ID que Android genera
//    automáticamente para tu archivo en res/font.
val lobsterFamily = FontFamily(
    Font(R.font.lobster_regular, FontWeight.Normal)
    // (Aquí podríamos añadir una versión "Bold" si la tuviéramos)
)

// 3. Actualizamos 'Typography' para usar la fuente
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default, // El cuerpo sigue siendo normal
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    // --- AÑADIMOS ESTOS ESTILOS NUEVOS ---

    // Lo usaremos para el título en la barra superior
    titleLarge = TextStyle(
        fontFamily = lobsterFamily, // Usamos Lobster
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp // Un buen tamaño para la barra
    ),

    // (Opcional) Un estilo para títulos más grandes en las pantallas
    headlineLarge = TextStyle(
        fontFamily = lobsterFamily, // Usamos Lobster
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp
    )
)
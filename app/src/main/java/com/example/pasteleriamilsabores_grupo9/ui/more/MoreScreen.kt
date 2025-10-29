package com.example.pasteleriamilsabores_grupo9.ui.more

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MoreScreen() {
    // Proximamente añadir enlaces a "Ayuda", "Políticas", etc.
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Pantalla 'Más' (Ayuda, Políticas, etc.)")
    }
}
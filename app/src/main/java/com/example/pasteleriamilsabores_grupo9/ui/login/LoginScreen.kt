package com.example.pasteleriamilsabores_grupo9.ui.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pasteleriamilsabores_grupo9.ui.theme.PasteleriaMilSabores_Grupo9Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(/* TODO: Add NavController later */) {
    Scaffold(topBar = { TopAppBar(title = { Text("Iniciar Sesión") }) }) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text("Pantalla de Login")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    PasteleriaMilSabores_Grupo9Theme {
        LoginScreen()
    }
}
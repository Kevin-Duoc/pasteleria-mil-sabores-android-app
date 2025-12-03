package com.example.pasteleriamilsabores_grupo9.ui.more

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.pasteleriamilsabores_grupo9.Routes

@Composable
fun MoreScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        ListItem(
            headlineContent = { Text("Nuestra Ubicación") },
            leadingContent = {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Nuestra Ubicación"
                )
            },
            modifier = Modifier.clickable {
                navController.navigate(Routes.MAP)
            }
        )
        ListItem(
            headlineContent = { Text("Ayuda") },
            leadingContent = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Help,
                    contentDescription = "Ayuda"
                )
            },
            modifier = Modifier.clickable { /* TODO: Navegar a pantalla de ayuda */ }
        )
        ListItem(
            headlineContent = { Text("Términos y Políticas") },
            leadingContent = {
                Icon(
                    imageVector = Icons.Default.Policy,
                    contentDescription = "Términos y Políticas"
                )
            },
            modifier = Modifier.clickable { /* TODO: Navegar a pantalla de políticas */ }
        )
    }
}
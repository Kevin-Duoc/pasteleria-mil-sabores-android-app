package com.example.pasteleriamilsabores_grupo9.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pasteleriamilsabores_grupo9.PasteleriaApplication
import com.example.pasteleriamilsabores_grupo9.Routes // <-- 1. IMPORTAR RUTAS
import com.example.pasteleriamilsabores_grupo9.viewmodel.ProfileViewModel
import com.example.pasteleriamilsabores_grupo9.viewmodel.ProfileViewModelFactory

/**
 * Pantalla "Inteligente" - Conecta con el ViewModel.
 * Recibe el NavController principal para poder navegar a Login/Register.
 */
@Composable
fun ProfileScreen(
    navController: NavController // <-- 2. AÑADIMOS NAVCONTROLLER
) {
    // 3. Inyectamos el ViewModel usando la Fábrica
    val context = LocalContext.current
    // Obtenemos el repositorio de autenticación desde la Application
    val authRepository = (context.applicationContext as PasteleriaApplication).authRepository
    val factory = ProfileViewModelFactory(authRepository)
    val viewModel: ProfileViewModel = viewModel(factory = factory)

    // 4. Observamos el estado del usuario actual
    val currentUser by viewModel.currentUser.collectAsState()

    // 5. Llamamos a la pantalla "tonta" pasándole el estado y acciones
    ProfileContent(
        isLoggedIn = currentUser != null, // Le decimos si hay alguien logueado
        userName = currentUser?.nombre, // Pasamos el nombre si existe
        onLoginClick = {
            // Navegamos a la pantalla de Login
            navController.navigate(Routes.LOGIN)
        },
        onRegisterClick = {
            // Navegamos a la pantalla de Registro
            navController.navigate(Routes.REGISTER)
        },
        onLogoutClick = {
            // Le decimos al ViewModel que cierre sesión
            viewModel.logout()
        }
    )
}

/**
 * Pantalla "Tonta" - Solo dibuja la UI basada en el estado.
 */
@Composable
fun ProfileContent(
    isLoggedIn: Boolean,
    userName: String?,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (isLoggedIn) {
                // --- Vista si el usuario ESTÁ logueado ---
                Text(
                    text = "¡Hola, ${userName ?: "Usuario"}!", // Muestra el nombre
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(24.dp))
                // TODO: Añadir más opciones de perfil (Mis Pedidos, Editar Perfil, etc.)
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onLogoutClick) {
                    Text("Cerrar Sesión")
                }
            } else {
                // --- Vista si el usuario NO ESTÁ logueado ---
                Text(
                    text = "Accede a tu cuenta",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onLoginClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Iniciar Sesión")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onRegisterClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Registrarse")
                }
            }
        }
    }
}
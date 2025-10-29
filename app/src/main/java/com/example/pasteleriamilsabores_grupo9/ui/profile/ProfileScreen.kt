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
import com.example.pasteleriamilsabores_grupo9.Routes
import com.example.pasteleriamilsabores_grupo9.viewmodel.ProfileViewModel
import com.example.pasteleriamilsabores_grupo9.viewmodel.ProfileViewModelFactory

@Composable
fun ProfileScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val authRepository = (context.applicationContext as PasteleriaApplication).authRepository
    val factory = ProfileViewModelFactory(authRepository)
    val viewModel: ProfileViewModel = viewModel(factory = factory)
    val currentUser by viewModel.currentUser.collectAsState()

    ProfileContent(
        isLoggedIn = currentUser != null,
        userName = currentUser?.nombre,
        onLoginClick = {
            navController.navigate(Routes.LOGIN)
        },
        onRegisterClick = {
            navController.navigate(Routes.REGISTER)
        },
        onLogoutClick = {
            viewModel.logout()
        }
    )
}

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
                Text(
                    text = "¡Hola, ${userName ?: "Usuario"}!",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(24.dp))
                // TODO: Añadir más opciones de perfil (Mis Pedidos, Editar Perfil, etc.)
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onLogoutClick) {
                    Text("Cerrar Sesión")
                }
            } else {
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
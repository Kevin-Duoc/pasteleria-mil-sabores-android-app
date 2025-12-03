package com.example.pasteleriamilsabores_grupo9.ui.profile

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pasteleriamilsabores_grupo9.PasteleriaApplication
import com.example.pasteleriamilsabores_grupo9.R
import com.example.pasteleriamilsabores_grupo9.Routes
import com.example.pasteleriamilsabores_grupo9.viewmodel.ProfileViewModel
import com.example.pasteleriamilsabores_grupo9.viewmodel.ProfileViewModelFactory

@Composable
fun ProfileScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val application = context.applicationContext as PasteleriaApplication
    val factory = ProfileViewModelFactory(application.authRepository)
    val viewModel: ProfileViewModel = viewModel(factory = factory)
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    ProfileContent(
        isLoggedIn = currentUser != null,
        userName = currentUser?.nombre,
        fotoUri = currentUser?.fotoUri,
        onLoginClick = { navController.navigate(Routes.LOGIN) },
        onRegisterClick = { navController.navigate(Routes.REGISTER) },
        onLogoutClick = { viewModel.logout() },
        onChangePhotoClick = { }
    )
}

@Composable
fun ProfileContent(
    isLoggedIn: Boolean,
    userName: String?,
    fotoUri: String?,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onChangePhotoClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(), // Ocupa el ancho disponible
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Centra verticalmente el contenido
        ) {
            if (isLoggedIn) {
                AsyncImage(
                    model = if (fotoUri != null) Uri.parse(fotoUri) else R.drawable.generico_male,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "¡Hola, ${userName ?: "Usuario"}!",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedButton(
                    onClick = { /* TODO: Navegar a Mis Pedidos */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Mis Pedidos")
                }

                Spacer(modifier = Modifier.height(16.dp))

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

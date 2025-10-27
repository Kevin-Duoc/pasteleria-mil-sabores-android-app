package com.example.pasteleriamilsabores_grupo9.ui.register

import android.widget.Toast // Para mostrar mensajes cortos
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext // Para usar Toasts
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pasteleriamilsabores_grupo9.PasteleriaApplication
import com.example.pasteleriamilsabores_grupo9.ui.theme.PasteleriaMilSabores_Grupo9Theme
import com.example.pasteleriamilsabores_grupo9.viewmodel.RegisterViewModel
import com.example.pasteleriamilsabores_grupo9.viewmodel.RegisterViewModelFactory
import com.example.pasteleriamilsabores_grupo9.viewmodel.RegisterUiState // <-- ¡AÑADIR ESTE IMPORT!
import kotlinx.coroutines.launch // Para usar corutinas en LaunchedEffect

/**
 * Pantalla "Inteligente" - Conecta con el ViewModel.
 */
@Composable
fun RegisterScreen(
    navController: NavController
) {
    // 1. Inyectamos el ViewModel
    val context = LocalContext.current
    val factory = RegisterViewModelFactory(
        (context.applicationContext as PasteleriaApplication).authRepository
    )
    val viewModel: RegisterViewModel = viewModel(factory = factory)

    // 2. Observamos el estado de la UI
    val uiState by viewModel.uiState.collectAsState()

    // 3. Efecto para manejar éxito o error (se ejecuta cuando uiState cambia)
    LaunchedEffect(uiState) {
        if (uiState.isSuccess) {
            // Éxito: Mostrar mensaje y volver a la pantalla anterior (ProfileScreen)
            Toast.makeText(context, "¡Registro exitoso!", Toast.LENGTH_SHORT).show()
            navController.popBackStack() // Vuelve atrás
        }
        uiState.error?.let { errorMessage ->
            // Error: Mostrar mensaje y limpiar el error en el ViewModel
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            viewModel.clearError() // Importante para no mostrar el error de nuevo
        }
    }

    // 4. Llamamos a la pantalla "tonta" pasándole el estado y acciones
    RegisterContent(
        uiState = uiState,
        navController = navController,
        onRegisterClick = { name, email, password ->
            // Le decimos al ViewModel que intente registrar
            viewModel.register(name, email, password)
        }
    )
}

/**
 * Pantalla "Tonta" - Solo dibuja la UI.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterContent(
    uiState: RegisterUiState, // Recibe el estado actual
    navController: NavController,
    onRegisterClick: (String, String, String) -> Unit // Callback para el botón
) {
    // Variables de estado para los campos (igual que antes)
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Cuenta") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) { // Usamos Box para superponer el loader
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Regístrate", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre Completo") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !uiState.isLoading // Deshabilitar si está cargando
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo Electrónico") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    enabled = !uiState.isLoading
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    enabled = !uiState.isLoading
                )
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        // Llamamos al callback pasándole los valores actuales
                        onRegisterClick(name, email, password)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = !uiState.isLoading // Deshabilitar botón si está cargando
                ) {
                    Text("Registrarse")
                }
            }

            // 5. Indicador de Carga (se muestra encima si isLoading es true)
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    PasteleriaMilSabores_Grupo9Theme {
        // La preview usa RegisterContent directamente
        RegisterContent(
            uiState = RegisterUiState(), // Estado inicial (sin carga ni error)
            navController = rememberNavController(),
            onRegisterClick = { _, _, _ -> } // Acción vacía para preview
        )
    }
}
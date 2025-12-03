package com.example.pasteleriamilsabores_grupo9.ui.login

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pasteleriamilsabores_grupo9.PasteleriaApplication
import com.example.pasteleriamilsabores_grupo9.ui.theme.PasteleriaMilSabores_Grupo9Theme
import com.example.pasteleriamilsabores_grupo9.viewmodel.LoginViewModel
import com.example.pasteleriamilsabores_grupo9.viewmodel.LoginViewModelFactory
import com.example.pasteleriamilsabores_grupo9.viewmodel.LoginUiState
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val factory = LoginViewModelFactory(
        (context.applicationContext as PasteleriaApplication).authRepository
    )
    val viewModel: LoginViewModel = viewModel(factory = factory)

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState.isSuccess) {
            Toast.makeText(context, "¡Inicio de sesión exitoso!", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
        uiState.error?.let { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    LoginContent(
        uiState = uiState,
        navController = navController,
        onLoginClick = { email, password ->
            viewModel.login(email, password)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(
    uiState: LoginUiState,
    navController: NavController,
    onLoginClick: (String, String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Iniciar Sesión") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Bienvenido de Nuevo", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo Electrónico") },
                    modifier = Modifier.fillMaxWidth().testTag("emailField"),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    enabled = !uiState.isLoading
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth().testTag("passwordField"),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    enabled = !uiState.isLoading
                )
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        onLoginClick(email, password)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("loginButton"),
                    enabled = !uiState.isLoading
                ) {
                    Text("Iniciar Sesión")
                }
                // TODO: Añadir enlace "¿Olvidaste tu contraseña?" o "Registrarse" si se necesita
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    PasteleriaMilSabores_Grupo9Theme {
        LoginContent(
            uiState = LoginUiState(),
            navController = rememberNavController(),
            onLoginClick = { _, _ -> }
        )
    }
}

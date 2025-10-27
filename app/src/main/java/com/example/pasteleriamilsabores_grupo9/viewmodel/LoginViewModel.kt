package com.example.pasteleriamilsabores_grupo9.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilsabores_grupo9.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Data class para guardar el estado de la pantalla de Login
data class LoginUiState(
    val isLoading: Boolean = false, // ¿Está cargando?
    val error: String? = null, // Guarda mensajes de error para la UI
    val isSuccess: Boolean = false // Se vuelve true si el login fue exitoso
)

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    // Estado interno mutable
    private val _uiState = MutableStateFlow(LoginUiState())
    // Estado público inmutable
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /**
     * Intenta iniciar sesión con email y contraseña.
     * Actualiza el estado de la UI según el resultado.
     */
    fun login(email: String, contrasena: String) {
        // Validación básica
        if (email.isBlank() || contrasena.isBlank()) {
            _uiState.update { it.copy(error = "Correo y contraseña son obligatorios.") }
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.update { it.copy(error = "Formato de correo inválido.") }
            return
        }

        // Iniciar estado de carga
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val success = authRepository.login(email, contrasena)
            if (success) {
                // Actualizar estado en caso de éxito
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } else {
                // Actualizar estado en caso de fallo (credenciales incorrectas)
                _uiState.update { it.copy(isLoading = false, error = "Correo o contraseña incorrectos.") }
            }
        }
    }

    /**
     * Reinicia el mensaje de error.
     */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

/**
 * Fábrica para crear el LoginViewModel.
 */
class LoginViewModelFactory(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Clase de ViewModel desconocida")
    }
}
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

// Data class para guardar el estado de la pantalla de registro
data class RegisterUiState(
    val isLoading: Boolean = false, // ¿Está cargando?
    val error: String? = null, // Guarda mensajes de error para la UI
    val isSuccess: Boolean = false // Se vuelve true si el registro fue exitoso
)

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    // Estado interno mutable (solo el ViewModel lo cambia)
    private val _uiState = MutableStateFlow(RegisterUiState())
    // Estado público inmutable (la UI solo lo lee)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    /**
     * Intenta registrar un usuario.
     * Actualiza el estado de la UI según el resultado (cargando, éxito, error).
     */
    fun register(nombre: String, email: String, contrasena: String) {
        // Validación básica (puedes añadir reglas más complejas aquí)
        if (nombre.isBlank() || email.isBlank() || contrasena.isBlank()) {
            _uiState.update { it.copy(error = "Todos los campos son obligatorios.") }
            return // Detiene la función si hay campos vacíos
        }
        // Validación de formato de email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.update { it.copy(error = "Formato de correo inválido.") }
            return
        }
        // Validación de largo de contraseña
        if (contrasena.length < 6) {
            _uiState.update { it.copy(error = "La contraseña debe tener al menos 6 caracteres.") }
            return
        }

        // Iniciar estado de carga
        _uiState.update { it.copy(isLoading = true, error = null) } // Pone isLoading=true y borra errores previos

        viewModelScope.launch { // Ejecuta la llamada al repositorio en segundo plano
            val success = authRepository.register(nombre, email, contrasena)
            if (success) {
                // Actualizar estado en caso de éxito
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } else {
                // Actualizar estado en caso de fallo (probablemente email duplicado)
                _uiState.update { it.copy(isLoading = false, error = "El correo electrónico ya está registrado.") }
            }
        }
    }

    /**
     * Reinicia el mensaje de error, típicamente llamado después de que la UI lo muestra.
     */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

/**
 * Fábrica para crear el RegisterViewModel.
 */
class RegisterViewModelFactory(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Clase de ViewModel desconocida")
    }
}
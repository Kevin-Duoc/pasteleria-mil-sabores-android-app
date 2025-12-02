package com.example.pasteleriamilsabores_grupo9.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilsabores_grupo9.data.remote.dto.RegisterRequest
import com.example.pasteleriamilsabores_grupo9.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun register(nombre: String, email: String, contrasena: String) {
        if (nombre.isBlank() || email.isBlank() || contrasena.isBlank()) {
            _uiState.update { it.copy(error = "Todos los campos son obligatorios.") }
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.update { it.copy(error = "Formato de correo inválido.") }
            return
        }
        if (contrasena.length < 6) {
            _uiState.update { it.copy(error = "La contraseña debe tener al menos 6 caracteres.") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            // Creamos el objeto de solicitud para la API
            val registerRequest = RegisterRequest(
                nombreCompleto = nombre,
                correo = email,
                contrasena = contrasena
                // Los otros campos (teléfono, etc.) se envían como null,
                // ya que la pantalla de registro actual no los solicita.
            )

            val success = authRepository.register(registerRequest)
            if (success) {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } else {
                // El error puede ser por correo duplicado o un fallo de red.
                _uiState.update { it.copy(isLoading = false, error = "No se pudo completar el registro. El correo podría ya estar en uso.") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

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
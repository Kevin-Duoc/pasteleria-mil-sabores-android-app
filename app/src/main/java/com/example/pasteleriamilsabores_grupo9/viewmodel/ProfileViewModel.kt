package com.example.pasteleriamilsabores_grupo9.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilsabores_grupo9.data.model.Usuario
import com.example.pasteleriamilsabores_grupo9.data.remote.dto.UpdateProfileRequest
import com.example.pasteleriamilsabores_grupo9.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class ProfileViewModel(private val authRepository: AuthRepository) : ViewModel() {

    // Se convierte el flujo "frío" en un flujo "caliente" y robusto.
    // Esto asegura que la pantalla siempre reciba el valor más reciente del usuario.
    val currentUser: StateFlow<Usuario?> = authRepository.currentUser.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000), // Inicia cuando la UI está visible
        initialValue = null // El valor inicial es nulo, como antes
    )

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun logout() {
        authRepository.logout()
    }

    fun updateProfile(nombre: String, telefono: String, fechaNac: String, nuevaContrasena: String?) {
        val usuarioId = currentUser.value?.id?.toLong() ?: return // No hacer nada si no hay usuario

        _uiState.update { it.copy(isLoading = true, error = null, isSuccess = false) }

        viewModelScope.launch {
            val updateRequest = UpdateProfileRequest(
                idUsuario = usuarioId,
                nombreCompleto = nombre,
                telefono = telefono,
                fechaNacimiento = fechaNac.ifBlank { null },
                contrasena = nuevaContrasena?.ifBlank { null }
            )

            val success = authRepository.updateUser(updateRequest)
            if (success) {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } else {
                _uiState.update { it.copy(isLoading = false, error = "No se pudo actualizar el perfil.") }
            }
        }
    }

    fun clearStatus() {
        _uiState.update { ProfileUiState() }
    }
}

class ProfileViewModelFactory(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

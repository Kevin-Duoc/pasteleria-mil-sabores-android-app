package com.example.pasteleriamilsabores_grupo9.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope // Needed for future suspend functions if added
import com.example.pasteleriamilsabores_grupo9.data.model.Usuario
import com.example.pasteleriamilsabores_grupo9.repository.AuthRepository
import kotlinx.coroutines.flow.StateFlow
// Removed unnecessary kotlinx.coroutines.launch import for now

/**
 * ViewModel para ProfileScreen.
 * Observa el estado de autenticación desde AuthRepository.
 */
class ProfileViewModel(private val authRepository: AuthRepository) : ViewModel() {

    // --- 1. Expone el estado del usuario actual ---
    // Expone directamente el StateFlow 'currentUser' desde AuthRepository.
    // La UI (ProfileScreen) observará esto para saber si alguien inició sesión.
    val currentUser: StateFlow<Usuario?> = authRepository.currentUser

    // --- 2. Función de Logout ---
    /**
     * Llama a la función logout en AuthRepository.
     */
    fun logout() {
        // No se necesita viewModelScope.launch aquí, ya que logout es una operación síncrona simple
        authRepository.logout()
    }

    // Nota: La lógica de Login y Registro probablemente se manejará en
    // LoginViewModel y RegisterViewModel separados más adelante, que también usarán AuthRepository.
    // Este ProfileViewModel se enfoca principalmente en MOSTRAR el estado y manejar el logout.
}

/**
 * Fábrica para crear el ProfileViewModel.
 * Requerida porque el ViewModel necesita AuthRepository en su constructor.
 */
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
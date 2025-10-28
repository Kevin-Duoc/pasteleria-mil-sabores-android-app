package com.example.pasteleriamilsabores_grupo9.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pasteleriamilsabores_grupo9.data.model.Usuario
import com.example.pasteleriamilsabores_grupo9.repository.AuthRepository
import kotlinx.coroutines.flow.StateFlow

/**
 * ViewModel para ProfileScreen.
 * Observa el estado de autenticación desde AuthRepository.
 */
class ProfileViewModel(private val authRepository: AuthRepository) : ViewModel() {

    val currentUser: StateFlow<Usuario?> = authRepository.currentUser

    /**
     * Llama a la función logout en AuthRepository.
     */
    fun logout() {
        authRepository.logout()
    }
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
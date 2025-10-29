package com.example.pasteleriamilsabores_grupo9.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilsabores_grupo9.data.model.Usuario
import com.example.pasteleriamilsabores_grupo9.repository.AuthRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val authRepository: AuthRepository) : ViewModel() {

    val currentUser: StateFlow<Usuario?> = authRepository.currentUser

    fun logout() {
        authRepository.logout()
    }

    fun updateUserImage(uri: Uri) {
        viewModelScope.launch {
            authRepository.updateUserImage(uri.toString())
        }
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
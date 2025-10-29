package com.example.pasteleriamilsabores_grupo9.repository

import com.example.pasteleriamilsabores_grupo9.data.dao.UsuarioDao
import com.example.pasteleriamilsabores_grupo9.data.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthRepository(private val usuarioDao: UsuarioDao) {

    private val _currentUser = MutableStateFlow<Usuario?>(null)
    val currentUser: StateFlow<Usuario?> = _currentUser.asStateFlow()

    suspend fun register(nombre: String, email: String, contrasena: String): Boolean {
        return try {
            val nuevoUsuario = Usuario(nombre = nombre, email = email, contrasena = contrasena, fotoUri = null)
            usuarioDao.insertUsuario(nuevoUsuario)

            val usuarioRegistrado = usuarioDao.getUsuarioByEmail(email)
            _currentUser.value = usuarioRegistrado
            true
        } catch (e: Exception) {
            _currentUser.value = null
            false
        }
    }

    suspend fun login(email: String, contrasena: String): Boolean {
        val usuarioEncontrado = usuarioDao.getUsuarioByEmail(email)
        return if (usuarioEncontrado != null && usuarioEncontrado.contrasena == contrasena) {
            _currentUser.value = usuarioEncontrado
            true
        } else {
            _currentUser.value = null
            false
        }
    }

    fun logout() {
        _currentUser.value = null
    }

    suspend fun updateUserImage(fotoUri: String) {
        val usuarioActual = _currentUser.value
        if (usuarioActual != null) {
            val usuarioActualizado = usuarioActual.copy(fotoUri = fotoUri)

            usuarioDao.updateUsuario(usuarioActualizado)

            _currentUser.value = usuarioActualizado
        }
    }

    fun isUserLoggedIn(): Boolean {
        return _currentUser.value != null
    }
}
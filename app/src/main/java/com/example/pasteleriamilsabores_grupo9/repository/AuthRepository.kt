package com.example.pasteleriamilsabores_grupo9.repository

import com.example.pasteleriamilsabores_grupo9.data.dao.UsuarioDao
import com.example.pasteleriamilsabores_grupo9.data.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * El Repositorio de Autenticación.
 * Maneja el login, registro y el estado del usuario actual.
 */
class AuthRepository(private val usuarioDao: UsuarioDao) {

    private val _currentUser = MutableStateFlow<Usuario?>(null)
    val currentUser: StateFlow<Usuario?> = _currentUser.asStateFlow()

    /**
     * Intenta registrar un nuevo usuario.
     * Si es exitoso, INICIA SESIÓN automáticamente con ese usuario.
     * Devuelve 'true' si fue exitoso, 'false' si el email ya existe.
     */
    suspend fun register(nombre: String, email: String, contrasena: String): Boolean {
        return try {
            // Creamos el objeto Usuario (el ID será 0 inicialmente)
            val nuevoUsuario = Usuario(nombre = nombre, email = email, contrasena = contrasena)
            // Intentamos insertarlo en la base de datos
            usuarioDao.insertUsuario(nuevoUsuario)
            _currentUser.value = nuevoUsuario
            true
        } catch (e: Exception) {
            _currentUser.value = null
            false
        }
    }

    /**
     * Intenta iniciar sesión con email y contraseña.
     * Si las credenciales son correctas, actualiza '_currentUser'.
     * Devuelve 'true' si el login fue exitoso, 'false' si no.
     */
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

    /**
     * Cierra la sesión del usuario actual.
     */
    fun logout() {
        _currentUser.value = null
    }

    /**
     * Función simple para verificar si hay alguien logueado.
     */
    fun isUserLoggedIn(): Boolean {
        return _currentUser.value != null
    }
}
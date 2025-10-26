package com.example.pasteleriamilsabores_grupo9.repository

import com.example.pasteleriamilsabores_grupo9.data.dao.UsuarioDao
import com.example.pasteleriamilsabores_grupo9.data.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * El Repositorio (Gerente) de Autenticación.
 * Maneja el login, registro y el estado del usuario actual.
 */
class AuthRepository(private val usuarioDao: UsuarioDao) {

    // --- Estado del Usuario Actual ---
    // Usamos MutableStateFlow para guardar quién está logueado.
    // Empieza como 'null' (nadie logueado).
    // Es PRIVADO para que solo el repositorio lo pueda cambiar.
    private val _currentUser = MutableStateFlow<Usuario?>(null)

    // Esta es la versión PÚBLICA e INMUTABLE.
    // Los ViewModels (como ProfileViewModel) observarán este 'Flow'
    // para saber si hay alguien logueado y reaccionar.
    val currentUser: StateFlow<Usuario?> = _currentUser.asStateFlow()

    /**
     * Intenta registrar un nuevo usuario.
     * Devuelve 'true' si fue exitoso, 'false' si el email ya existe.
     */
    suspend fun register(nombre: String, email: String, contrasena: String): Boolean {
        return try {
            val nuevoUsuario = Usuario(nombre = nombre, email = email, contrasena = contrasena)
            usuarioDao.insertUsuario(nuevoUsuario)
            // Si insertUsuario no lanzó excepción (email no repetido), fue exitoso.
            true
        } catch (e: Exception) {
            // Si hubo una excepción (probablemente email duplicado), falló.
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
            // ¡Credenciales correctas! Actualizamos el estado.
            _currentUser.value = usuarioEncontrado
            true
        } else {
            // Credenciales incorrectas o email no encontrado.
            _currentUser.value = null // Aseguramos que nadie quede logueado por error
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
     * (Útil para proteger rutas o mostrar/ocultar botones)
     */
    fun isUserLoggedIn(): Boolean {
        return _currentUser.value != null
    }
}
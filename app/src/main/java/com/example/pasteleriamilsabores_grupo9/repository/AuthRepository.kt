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

            // --- 👇 LÍNEA NUEVA AÑADIDA 👇 ---
            // Si la inserción fue exitosa (no hubo excepción),
            // actualizamos el estado para iniciar sesión con este nuevo usuario.
            // Nota: '_currentUser' tendrá el ID=0, lo cual está bien para la sesión,
            // pero si necesitáramos el ID real asignado por la BD, tendríamos
            // que volver a buscarlo con getUsuarioByEmail. Para este caso, no es necesario.
            _currentUser.value = nuevoUsuario
            // --- 👆 FIN DE LÍNEA NUEVA 👆 ---

            true // Devolvemos 'true' indicando éxito
        } catch (e: Exception) {
            // Si hubo una excepción (probablemente email duplicado), falló.
            _currentUser.value = null // Aseguramos que nadie quede logueado por error
            false // Devolvemos 'false'
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
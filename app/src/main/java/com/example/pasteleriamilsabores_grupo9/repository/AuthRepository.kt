package com.example.pasteleriamilsabores_grupo9.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.pasteleriamilsabores_grupo9.data.model.Usuario
import com.example.pasteleriamilsabores_grupo9.data.remote.api.AuthApiService // <-- RUTA CORREGIDA
import com.example.pasteleriamilsabores_grupo9.data.remote.dto.LoginRequest
import com.example.pasteleriamilsabores_grupo9.data.remote.dto.RegisterRequest
import com.example.pasteleriamilsabores_grupo9.data.remote.dto.UpdateProfileRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

class AuthRepository(
    private val authApiService: AuthApiService,
    private val context: Context,
    private val externalScope: CoroutineScope
) {

    private val _currentUser = MutableStateFlow<Usuario?>(null)
    val currentUser: StateFlow<Usuario?> = _currentUser.asStateFlow().stateIn(
        scope = externalScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    private var authToken: String? = null

    private val prefs: SharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            "session_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    init {
        loadSession()
    }

    private fun loadSession() {
        authToken = prefs.getString("auth_token", null)
        val userId = prefs.getInt("user_id", -1)
        val userName = prefs.getString("user_name", null)
        val userEmail = prefs.getString("user_email", null)

        if (authToken != null && userId != -1 && userName != null && userEmail != null) {
            _currentUser.value = Usuario(
                id = userId,
                nombre = userName,
                email = userEmail,
                contrasena = ""
            )
        }
    }

    suspend fun register(registerRequest: RegisterRequest) {
        val response = authApiService.register(registerRequest)
        if (!response.isSuccessful) {
            val errorMsg = response.errorBody()?.string()
            val exceptionMsg = if (errorMsg.isNullOrBlank()) {
                "Error en el registro (código: ${response.code()})"
            } else {
                errorMsg
            }
            throw Exception(exceptionMsg)
        }
    }

    suspend fun login(email: String, contrasena: String) {
        try {
            val loginRequest = LoginRequest(correo = email, contrasena = contrasena)
            val loginResponse = authApiService.login(loginRequest)

            val usuario = Usuario(
                id = loginResponse.idUsuario,
                nombre = loginResponse.nombre,
                email = email,
                contrasena = ""
            )
            _currentUser.value = usuario
            saveSession(loginResponse.token, usuario)
        } catch (e: Exception) {
            throw Exception("Correo o contraseña incorrectos", e)
        }
    }

    private fun saveSession(token: String, usuario: Usuario) {
        authToken = token
        prefs.edit()
            .putString("auth_token", token)
            .putInt("user_id", usuario.id)
            .putString("user_name", usuario.nombre)
            .putString("user_email", usuario.email)
            .apply()
    }

    fun logout() {
        _currentUser.value = null
        authToken = null
        prefs.edit().clear().apply()
    }

    suspend fun updateUser(updateRequest: UpdateProfileRequest): Boolean {
        val response = authApiService.updateUser(updateRequest)
        if (response.isSuccessful) {
            val usuarioActual = _currentUser.value
            val tokenActual = authToken
            if (usuarioActual != null && tokenActual != null) {
                val usuarioActualizado = usuarioActual.copy(
                    nombre = updateRequest.nombreCompleto ?: usuarioActual.nombre
                )
                _currentUser.value = usuarioActualizado
                saveSession(tokenActual, usuarioActualizado)
            }
            return true
        } else {
            return false
        }
    }

    fun isUserLoggedIn(): Boolean {
        return _currentUser.value != null
    }

    fun getAuthToken(): String? {
        return authToken
    }
}

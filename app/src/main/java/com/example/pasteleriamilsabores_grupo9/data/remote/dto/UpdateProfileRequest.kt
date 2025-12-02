package com.example.pasteleriamilsabores_grupo9.data.remote.dto

// Contiene los campos que se pueden actualizar desde el perfil
data class UpdateProfileRequest(
    val idUsuario: Long,
    val nombreCompleto: String? = null,
    val telefono: String? = null,
    val fechaNacimiento: String? = null, // Se envía como String en formato "YYYY-MM-DD"
    val idRegion: Long? = null,
    val idComuna: Long? = null,
    val contrasena: String? = null // Opcional, solo si el usuario quiere cambiarla
)

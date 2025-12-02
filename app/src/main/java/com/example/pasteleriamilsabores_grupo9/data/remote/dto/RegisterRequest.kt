package com.example.pasteleriamilsabores_grupo9.data.remote.dto

import java.time.LocalDate

// Basado en la entidad Usuario.java del backend
data class RegisterRequest(
    val nombreCompleto: String,
    val correo: String,
    val contrasena: String,
    val rol: String = "CLIENTE", // Por defecto, los registros desde la app son de clientes
    val telefono: String? = null,
    val fechaNacimiento: String? = null, // Se envía como String en formato "YYYY-MM-DD"
    val idRegion: Long? = null,
    val idComuna: Long? = null
)

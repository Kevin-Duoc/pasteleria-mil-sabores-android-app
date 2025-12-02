package com.example.pasteleriamilsabores_grupo9.data.remote.dto

data class LoginResponse(
    val token: String,
    val rol: String,
    val nombre: String,
    val idUsuario: Int
)

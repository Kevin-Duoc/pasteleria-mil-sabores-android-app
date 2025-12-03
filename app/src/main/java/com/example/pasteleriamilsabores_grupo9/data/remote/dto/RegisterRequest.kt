package com.example.pasteleriamilsabores_grupo9.data.remote.dto

import com.google.gson.annotations.SerializedName

// Este archivo ahora es un calco de la petición que funciona en la web.
data class RegisterRequest(
    @SerializedName("nombreCompleto")
    val nombreCompleto: String,

    @SerializedName("correo")
    val correo: String,

    @SerializedName("contrasena")
    val contrasena: String,

    @SerializedName("telefono")
    val telefono: String,

    @SerializedName("fechaNacimiento")
    val fechaNacimiento: String,

    @SerializedName("rol")
    val rol: String = "CLIENTE",

    // Usando los IDs que funcionan en la web.
    @SerializedName("idRegion")
    val idRegion: Int = 1,

    @SerializedName("idComuna")
    val idComuna: Int = 2
)

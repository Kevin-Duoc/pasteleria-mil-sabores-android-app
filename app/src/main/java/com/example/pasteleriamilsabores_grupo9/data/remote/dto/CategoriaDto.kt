package com.example.pasteleriamilsabores_grupo9.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CategoriaDto(
    @SerializedName("idCategoria")
    val idCategoria: Long,
    @SerializedName("nombreCategoria")
    val nombreCategoria: String? // Permite que el nombre sea nulo
)

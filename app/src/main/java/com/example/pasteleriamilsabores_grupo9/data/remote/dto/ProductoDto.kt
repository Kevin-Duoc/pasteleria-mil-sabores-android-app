package com.example.pasteleriamilsabores_grupo9.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductoDto(
    @SerializedName("idProducto")
    val idProducto: Long,
    @SerializedName("codigoSku")
    val codigoSku: String,
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("descripcion")
    val descripcion: String,
    @SerializedName("precio")
    val precio: Int,
    @SerializedName("urlImagen")
    val urlImagen: String,
    @SerializedName("stock")
    val stock: Int,
    @SerializedName("stockCritico")
    val stockCritico: Int,
    @SerializedName("idCategoria")
    val idCategoria: Long,
    @SerializedName("nombreCategoria")
    val nombreCategoria: String
)

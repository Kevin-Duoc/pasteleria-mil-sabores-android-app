package com.example.pasteleriamilsabores_grupo9.data.remote.dto.pedidos

import com.google.gson.annotations.SerializedName

/**
 * DTO para el detalle de un producto recibido desde la API de pedidos.
 * Representa un item dentro de la lista de detalles de un pedido.
 */
data class DetallePedidoResponseDto(

    @SerializedName("idDetalle")
    val idDetalle: Long,

    @SerializedName("nombreProducto")
    val nombreProducto: String,

    @SerializedName("precioUnitario")
    val precioUnitario: Int,

    @SerializedName("cantidad")
    val cantidad: Int
)

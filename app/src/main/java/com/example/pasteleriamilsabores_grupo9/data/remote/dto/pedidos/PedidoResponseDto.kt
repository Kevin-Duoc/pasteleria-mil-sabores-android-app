package com.example.pasteleriamilsabores_grupo9.data.remote.dto.pedidos

import com.google.gson.annotations.SerializedName

/**
 * DTO para la respuesta de un pedido individual desde la API.
 * Contiene la información completa de un pedido, incluyendo sus detalles.
 */
data class PedidoResponseDto(

    @SerializedName("idPedido")
    val idPedido: Long,

    @SerializedName("fecha")
    val fecha: String,

    @SerializedName("estado")
    val estado: String,

    @SerializedName("total")
    val total: Int,

    @SerializedName("detalles")
    val detalles: List<DetallePedidoResponseDto>
)

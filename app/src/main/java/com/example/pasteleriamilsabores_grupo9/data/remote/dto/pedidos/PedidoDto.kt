package com.example.pasteleriamilsabores_grupo9.data.remote.dto.pedidos

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object para crear un nuevo pedido.
 * Esta es la estructura principal que se enviará a ms-pedidos.
 */
data class PedidoDto(

    @SerializedName("idUsuarioRef")
    val idUsuarioRef: Long,

    @SerializedName("total")
    val total: Int,

    @SerializedName("detalles")
    val detalles: List<DetallePedidoDto>
)

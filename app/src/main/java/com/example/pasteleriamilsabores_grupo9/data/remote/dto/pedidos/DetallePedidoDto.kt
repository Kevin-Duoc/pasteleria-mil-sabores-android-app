package com.example.pasteleriamilsabores_grupo9.data.remote.dto.pedidos

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object para el detalle de un producto dentro de un pedido.
 * Esta es la estructura que se enviará a ms-pedidos para cada item del carrito.
 */
data class DetallePedidoDto(

    @SerializedName("idProductoRef")
    val idProductoRef: Long,

    @SerializedName("nombreProducto")
    val nombreProducto: String,

    @SerializedName("precioUnitario")
    val precioUnitario: Int,

    @SerializedName("cantidad")
    val cantidad: Int
)

package com.example.pasteleriamilsabores_grupo9.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Define la estructura de la tabla para los items del carrito en la base de datos local (Room).
 * Cada instancia de esta clase representa un producto en el carrito de compras del usuario.
 */
@Entity(tableName = "carrito_table")
data class CarritoItem(

    // El ID del producto del catálogo. Es la clave primaria para asegurar que no hay duplicados.
    @PrimaryKey
    val productId: Long,

    // Nombre del producto, para mostrar en el carrito.
    val nombre: String,

    // Precio unitario del producto.
    val precio: Int,

    // Nombre del archivo de imagen (sin extensión) para mostrar en el carrito.
    val imagenUrl: String,

    // Cantidad de este producto que el usuario ha añadido al carrito.
    var cantidad: Int
)

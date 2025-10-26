package com.example.pasteleriamilsabores_grupo9.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Define la tabla 'carrito_items' en la base de datos.
 *
 * Esta tabla tendrá una "llave foránea" (foreign key) que la conecta
 * con la tabla 'productos'. Esto asegura que no podamos añadir
 * un producto al carrito si no existe en la tabla de productos.
 */
@Entity(
    tableName = "carrito_items",
    foreignKeys = [
        ForeignKey(
            entity = Producto::class,
            parentColumns = ["id_producto"], // Columna en la tabla 'productos'
            childColumns = ["producto_id"],  // Columna en esta tabla ('carrito_items')
            onDelete = ForeignKey.CASCADE // Si un producto se borra, también se borra del carrito
        )
    ]
)
data class ItemCarrito(
    // Llave primaria autoincremental para el ítem del carrito
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    val itemId: Int = 0,

    // El ID del producto (de la tabla 'productos') al que se refiere
    @ColumnInfo(name = "producto_id", index = true) // 'index = true' optimiza las búsquedas
    val productId: String,

    // Guardamos copias de estos datos para un acceso rápido en el carrito,
    // aunque técnicamente podríamos obtenerlos "uniendo" las tablas.
    // Esto se llama "desnormalización" y es útil en apps móviles.
    @ColumnInfo(name = "nombre_producto")
    val nombre: String,

    @ColumnInfo(name = "precio_producto")
    val precio: Int,

    @ColumnInfo(name = "cantidad")
    val cantidad: Int
)
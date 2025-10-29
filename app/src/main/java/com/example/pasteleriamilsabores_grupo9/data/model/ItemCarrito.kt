package com.example.pasteleriamilsabores_grupo9.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "carrito_items",
    foreignKeys = [
        ForeignKey(
            entity = Producto::class,
            parentColumns = ["id_producto"],
            childColumns = ["producto_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ItemCarrito(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    val itemId: Int = 0,

    @ColumnInfo(name = "producto_id", index = true)
    val productId: String,

    @ColumnInfo(name = "nombre_producto")
    val nombre: String,

    @ColumnInfo(name = "precio_producto")
    val precio: Int,

    @ColumnInfo(name = "cantidad")
    val cantidad: Int,

    @ColumnInfo(name = "imagen_res_name")
    val imagenResIdName: String
)
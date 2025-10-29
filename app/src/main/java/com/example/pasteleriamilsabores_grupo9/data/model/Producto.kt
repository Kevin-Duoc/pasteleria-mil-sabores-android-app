package com.example.pasteleriamilsabores_grupo9.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class Producto(

    @PrimaryKey
    @ColumnInfo(name = "id_producto")
    val id: String,

    @ColumnInfo(name = "nombre")
    val nombre: String,

    @ColumnInfo(name = "descripcion")
    val descripcion: String,

    @ColumnInfo(name = "precio")
    val precio: Int,

    @ColumnInfo(name = "imagen_res_name")
    val imagenResIdName: String,

    @ColumnInfo(name = "stock")
    val stock: Int,

    @ColumnInfo(name = "stock_critico")
    val stockCritico: Int
)
package com.example.pasteleriamilsabores_grupo9.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "usuarios",
    indices = [Index(value = ["email"], unique = true)] //no emails repetidos
)
data class Usuario(
    // ID autoincremental para el usuario
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "usuario_id")
    val id: Int = 0,

    @ColumnInfo(name = "nombre_completo")
    val nombre: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "contrasena")
    val contrasena: String
)
package com.example.pasteleriamilsabores_grupo9.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Define la tabla 'usuarios' en la base de datos.
 *
 * Añadimos un "índice único" al email para asegurarnos
 * de que no puedan existir dos usuarios con el mismo email.
 */
@Entity(
    tableName = "usuarios",
    indices = [Index(value = ["email"], unique = true)] // Regla de BD: no emails repetidos
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

    // Guardaremos la contraseña.
    // (En una app real, esto debería estar encriptado,
    // pero para este proyecto lo guardamos como texto simple).
    @ColumnInfo(name = "contrasena")
    val contrasena: String
)
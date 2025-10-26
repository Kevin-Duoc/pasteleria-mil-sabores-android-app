package com.example.pasteleriamilsabores_grupo9.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pasteleriamilsabores_grupo9.data.model.Usuario

@Dao
interface UsuarioDao {

    /**
     * Inserta un nuevo usuario.
     * OnConflictStrategy.ABORT significa que si el email ya existe
     * (debido al índice único en la tabla Usuario), la operación fallará
     * y lanzará una excepción. Esto es lo que queremos para el registro.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUsuario(usuario: Usuario)

    /**
     * Busca un usuario por su email.
     * Se usa para el proceso de Login.
     * Devuelve 'null' si no se encuentra el email.
     */
    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun getUsuarioByEmail(email: String): Usuario?

    // (Podríamos añadir más funciones después, como actualizar perfil, etc.)
}
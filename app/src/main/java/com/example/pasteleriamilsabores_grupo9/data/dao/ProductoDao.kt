package com.example.pasteleriamilsabores_grupo9.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pasteleriamilsabores_grupo9.data.model.Producto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {

    // --- ESCRITURA ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(productos: List<Producto>)

    // --- LECTURA ---
    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    fun getAllProductos(): Flow<List<Producto>>

    @Query("SELECT * FROM productos WHERE id_producto = :id")
    fun getProductoById(id: String): Flow<Producto>

    /**
     * Cuenta cuántos productos hay en la tabla.
     * Esta es la función clave para saber si necesitamos poblar la BD.
     */
    @Query("SELECT COUNT(*) FROM productos")
    suspend fun count(): Int
}
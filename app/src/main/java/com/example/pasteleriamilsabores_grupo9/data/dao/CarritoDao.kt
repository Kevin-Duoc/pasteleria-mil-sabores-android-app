package com.example.pasteleriamilsabores_grupo9.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pasteleriamilsabores_grupo9.data.model.ItemCarrito
import kotlinx.coroutines.flow.Flow

@Dao
interface CarritoDao {

    /**
     * Inserta un nuevo item en el carrito.
     * Si ya existe (basado en la PrimaryKey), lo reemplaza.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ItemCarrito)

    /**
     * Actualiza un item existente en el carrito.
     * Room usa la PrimaryKey del 'item' para saber cuál actualizar.
     */
    @Update
    suspend fun updateItem(item: ItemCarrito)

    /**
     * Elimina un item específico del carrito.
     */
    @Delete
    suspend fun deleteItem(item: ItemCarrito)

    /**
     * Busca un item en el carrito usando el ID del PRODUCTO.
     * Esta es la función clave para saber si ya añadimos esa torta.
     * No devuelve un Flow, solo una "foto" instantánea.
     * Puede devolver nulo (null) si no se encuentra.
     */
    @Query("SELECT * FROM carrito_items WHERE producto_id = :productId LIMIT 1")
    suspend fun getItemByProductId(productId: String): ItemCarrito?

    /**
     * Obtiene TODOS los items del carrito, ordenados por nombre.
     * Devuelve un Flow para que la pantalla del carrito se
     * actualice automáticamente.
     */
    @Query("SELECT * FROM carrito_items ORDER BY nombre_producto ASC")
    fun getAllItems(): Flow<List<ItemCarrito>>

    /**
     * Borra TODOS los items de la tabla 'carrito_items'.
     * Útil para cuando se completa una compra.
     */
    @Query("DELETE FROM carrito_items")
    suspend fun clearCart()
}
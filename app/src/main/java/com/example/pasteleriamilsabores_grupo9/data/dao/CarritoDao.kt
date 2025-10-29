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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ItemCarrito)

    @Update
    suspend fun updateItem(item: ItemCarrito)

    @Delete
    suspend fun deleteItem(item: ItemCarrito)

    @Query("SELECT * FROM carrito_items WHERE producto_id = :productId LIMIT 1")
    suspend fun getItemByProductId(productId: String): ItemCarrito?

    @Query("SELECT * FROM carrito_items ORDER BY nombre_producto ASC")
    fun getAllItems(): Flow<List<ItemCarrito>>

    @Query("DELETE FROM carrito_items")
    suspend fun clearCart()
}
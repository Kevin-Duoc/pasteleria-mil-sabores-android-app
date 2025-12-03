package com.example.pasteleriamilsabores_grupo9.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pasteleriamilsabores_grupo9.data.db.entity.CarritoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CarritoDao {

    // --- Operaciones Principales del Carrito ---

    /**
     * Inserta un nuevo item en el carrito.
     * Si el item ya existe (basado en su PrimaryKey), la operación se ignora.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: CarritoItem)

    /**
     * Actualiza un item existente en el carrito.
     * Es crucial para modificar la cantidad de un producto ya añadido.
     */
    @Update
    suspend fun update(item: CarritoItem)

    /**
     * Elimina un item específico del carrito.
     */
    @Delete
    suspend fun delete(item: CarritoItem)

    /**
     * Elimina TODOS los items del carrito. Útil para vaciar el carrito después de una compra.
     */
    @Query("DELETE FROM carrito_table")
    suspend fun deleteAll()

    // --- Consultas ---

    /**
     * Obtiene un item específico del carrito por su ID.
     * Marcada como 'suspend' para asegurar que se ejecuta fuera del hilo principal.
     *
     * @param productId El ID del producto a buscar.
     * @return El CarritoItem si se encuentra, o null si no.
     */
    @Query("SELECT * FROM carrito_table WHERE productId = :productId")
    suspend fun getItemById(productId: Long): CarritoItem?

    /**
     * Obtiene todos los items del carrito y los emite como un Flow.
     * La UI puede observar este Flow para actualizarse automáticamente cuando cambien los datos.
     */
    @Query("SELECT * FROM carrito_table ORDER BY nombre ASC")
    fun getAllItems(): Flow<List<CarritoItem>>
}

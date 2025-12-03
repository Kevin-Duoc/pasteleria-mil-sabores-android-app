package com.example.pasteleriamilsabores_grupo9.repository

import androidx.annotation.WorkerThread
import com.example.pasteleriamilsabores_grupo9.data.dao.CarritoDao
import com.example.pasteleriamilsabores_grupo9.data.db.entity.CarritoItem
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio para gestionar las operaciones del carrito de compras.
 * Se comunica con el CarritoDao para interactuar con la base de datos local.
 */
class CarritoRepository(private val carritoDao: CarritoDao) {

    // Flujo que emite la lista completa de items en el carrito. La UI se suscribirá a esto.
    val allItems: Flow<List<CarritoItem>> = carritoDao.getAllItems()

    /**
     * Añade un item al carrito o actualiza su cantidad si ya existe.
     *
     * @param item El CarritoItem que se va a añadir o actualizar.
     */
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun addToCart(item: CarritoItem) {
        // Busca si el item ya existe en la base de datos.
        val existingItem = carritoDao.getItemById(item.productId)

        if (existingItem == null) {
            // Si no existe, lo inserta como un nuevo item.
            carritoDao.insert(item)
        } else {
            // Si ya existe, actualiza su cantidad y luego actualiza el registro en la BD.
            existingItem.cantidad += item.cantidad
            carritoDao.update(existingItem)
        }
    }

    /**
     * Actualiza un item existente en el carrito. Usado para cambiar la cantidad desde la UI.
     *
     * @param item El CarritoItem con los datos actualizados.
     */
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(item: CarritoItem) {
        carritoDao.update(item)
    }

    /**
     * Elimina un item específico del carrito.
     *
     * @param item El CarritoItem a eliminar.
     */
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(item: CarritoItem) {
        carritoDao.delete(item)
    }

    /**
     * Vacía completamente el carrito de compras.
     */
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun clearCart() {
        carritoDao.deleteAll()
    }
}

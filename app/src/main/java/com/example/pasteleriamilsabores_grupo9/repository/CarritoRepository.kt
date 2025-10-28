package com.example.pasteleriamilsabores_grupo9.repository

import com.example.pasteleriamilsabores_grupo9.data.dao.CarritoDao
import com.example.pasteleriamilsabores_grupo9.data.model.ItemCarrito
import com.example.pasteleriamilsabores_grupo9.data.model.Producto
import kotlinx.coroutines.flow.Flow

/**
 * El Repositorio del Carrito.
 * Es el único que tiene acceso al 'CarritoDao'.
 */
class CarritoRepository(private val carritoDao: CarritoDao) {

    /**
     * Pide al DAO (Portero) el chorro (Flow) de todos los items del carrito.
     * La pantalla 'CartScreen' observará este Flow.
     */
    val allCartItems: Flow<List<ItemCarrito>> = carritoDao.getAllItems()

    /**
     * Revisa si un producto ya existe en el carrito.
     * Si existe, actualiza la cantidad.
     * Si no existe, inserta un nuevo item.
     */
    suspend fun addItemToCart(producto: Producto, cantidad: Int) {
        val itemExistente = carritoDao.getItemByProductId(producto.id)

        if (itemExistente == null) {
            val newItem = ItemCarrito(
                productId = producto.id,
                nombre = producto.nombre,
                precio = producto.precio,
                cantidad = cantidad
            )
            carritoDao.insertItem(newItem)
        } else {
            val updatedItem = itemExistente.copy(
                cantidad = itemExistente.cantidad + cantidad
            )
            carritoDao.updateItem(updatedItem)
        }
    }

    /**
     * Le pide al DAO que actualice un item.
     * (Útil para botones +/- en la pantalla del carrito)
     */
    suspend fun updateItem(item: ItemCarrito) {
        carritoDao.updateItem(item)
    }

    /**
     * Le pide al DAO que elimine un item.
     */
    suspend fun deleteItem(item: ItemCarrito) {
        carritoDao.deleteItem(item)
    }

    /**
     * Le pide al DAO que vacíe el carrito.
     */
    suspend fun clearCart() {
        carritoDao.clearCart()
    }
}
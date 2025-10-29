package com.example.pasteleriamilsabores_grupo9.repository

import com.example.pasteleriamilsabores_grupo9.data.dao.CarritoDao
import com.example.pasteleriamilsabores_grupo9.data.model.ItemCarrito
import com.example.pasteleriamilsabores_grupo9.data.model.Producto
import kotlinx.coroutines.flow.Flow

class CarritoRepository(private val carritoDao: CarritoDao) {

    val allCartItems: Flow<List<ItemCarrito>> = carritoDao.getAllItems()

    suspend fun addItemToCart(producto: Producto, cantidad: Int) {
        val itemExistente = carritoDao.getItemByProductId(producto.id)

        if (itemExistente == null) {
            val newItem = ItemCarrito(
                productId = producto.id,
                nombre = producto.nombre,
                precio = producto.precio,
                cantidad = cantidad,
                imagenResIdName = producto.imagenResIdName
            )
            carritoDao.insertItem(newItem)
        } else {
            val updatedItem = itemExistente.copy(
                cantidad = itemExistente.cantidad + cantidad
            )
            carritoDao.updateItem(updatedItem)
        }
    }

    suspend fun updateItem(item: ItemCarrito) {
        carritoDao.updateItem(item)
    }

    suspend fun deleteItem(item: ItemCarrito) {
        carritoDao.deleteItem(item)
    }

    suspend fun clearCart() {
        carritoDao.clearCart()
    }
}
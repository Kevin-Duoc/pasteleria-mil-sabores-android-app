package com.example.pasteleriamilsabores_grupo9.repository

import com.example.pasteleriamilsabores_grupo9.data.dao.ProductoDao
import com.example.pasteleriamilsabores_grupo9.data.model.Producto
import kotlinx.coroutines.flow.Flow

class ProductoRepository(private val productoDao: ProductoDao) {

    val allProductos: Flow<List<Producto>> = productoDao.getAllProductos()

    fun getProductoById(id: String): Flow<Producto> {
        return productoDao.getProductoById(id)
    }
}
package com.example.pasteleriamilsabores_grupo9.repository

import com.example.pasteleriamilsabores_grupo9.data.dao.ProductoDao
import com.example.pasteleriamilsabores_grupo9.data.model.Producto
import kotlinx.coroutines.flow.Flow

/**
 * El Repositorio (Gerente).
 * Es el único que tiene acceso al 'ProductoDao' (el Portero).
 * Sirve como la "única fuente de verdad" para los datos de productos.
 *
 * Recibe el dao en su constructor. Más adelante, usaremos "Inyección de Dependencias"
 * para proveer esto automáticamente, pero por ahora se lo pasaremos a mano.
 */
class ProductoRepository(private val productoDao: ProductoDao) {

    /**
     * Pide al DAO (Portero) el chorro (Flow) de todos los productos.
     * El ViewModel observará este Flow.
     */
    val allProductos: Flow<List<Producto>> = productoDao.getAllProductos()

    /**
     * Pide al DAO (Portero) el chorro (Flow) de un producto específico por su ID.
     * El ViewModel de Detalle observará este Flow.
     */
    fun getProductoById(id: String): Flow<Producto> {
        return productoDao.getProductoById(id)
    }

    // Aquí podríamos añadir más lógica de negocio, por ejemplo:
    // - Decidir si buscar productos en el DAO (local) o en una API (remoto).
    // - Combinar datos de diferentes fuentes.
}
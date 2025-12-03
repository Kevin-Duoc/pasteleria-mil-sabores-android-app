package com.example.pasteleriamilsabores_grupo9.repository

import com.example.pasteleriamilsabores_grupo9.data.remote.api.CatalogApiService // <-- RUTA CORREGIDA
import com.example.pasteleriamilsabores_grupo9.data.remote.dto.CategoriaDto
import com.example.pasteleriamilsabores_grupo9.data.remote.dto.ProductoDto

class CatalogRepository(private val catalogApiService: CatalogApiService) {

    suspend fun getProductos(): List<ProductoDto> {
        return catalogApiService.getProductos()
    }

    suspend fun getCategorias(): List<CategoriaDto> {
        return catalogApiService.getCategorias()
    }

    suspend fun getProductoById(id: Long): ProductoDto {
        return catalogApiService.getProductoById(id)
    }

    suspend fun getProductosByCategoria(idCategoria: Long): List<ProductoDto> {
        return catalogApiService.getProductosByCategoria(idCategoria)
    }
}

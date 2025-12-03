package com.example.pasteleriamilsabores_grupo9.data.remote.api

import com.example.pasteleriamilsabores_grupo9.data.remote.dto.CategoriaDto
import com.example.pasteleriamilsabores_grupo9.data.remote.dto.ProductoDto
import retrofit2.http.GET
import retrofit2.http.Path

interface CatalogApiService {

    @GET("api/v1/catalogo/productos")
    suspend fun getProductos(): List<ProductoDto>

    @GET("api/v1/catalogo/categorias")
    suspend fun getCategorias(): List<CategoriaDto>

    @GET("api/v1/catalogo/productos/{id}")
    suspend fun getProductoById(@Path("id") id: Long): ProductoDto

    @GET("api/v1/catalogo/productos/categoria/{idCategoria}")
    suspend fun getProductosByCategoria(@Path("idCategoria") idCategoria: Long): List<ProductoDto>
}

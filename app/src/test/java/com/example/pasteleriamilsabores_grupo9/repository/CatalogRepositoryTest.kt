
package com.example.pasteleriamilsabores_grupo9.repository

import com.example.pasteleriamilsabores_grupo9.data.remote.api.CatalogApiService
import com.example.pasteleriamilsabores_grupo9.data.remote.dto.CategoriaDto
import com.example.pasteleriamilsabores_grupo9.data.remote.dto.ProductoDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CatalogRepositoryTest {

    private lateinit var repository: CatalogRepository
    private lateinit var fakeApiService: FakeCatalogApiService

    private val sampleProducts = listOf(
        ProductoDto(101L, "SKU001", "Pastel de Chocolate", "", 25, "", 10, 5, 1L, "Pasteles")
    )
    private val sampleCategories = listOf(
        CategoriaDto(1, "Pasteles")
    )

    @Before
    fun setUp() {
        fakeApiService = FakeCatalogApiService()
        repository = CatalogRepository(fakeApiService)
    }

    @Test
    fun `getProductos - cuando api es exitosa - debe retornar lista de productos`() = runTest {
        // Arrange
        fakeApiService.productsToReturn = sampleProducts

        // Act
        val result = repository.getProductos()

        // Assert
        assertEquals(sampleProducts, result)
    }

    @Test
    fun `getProductos - cuando api falla - debe propagar la excepción`() = runTest {
        // Arrange
        val expectedExceptionMessage = "Error de red"
        fakeApiService.shouldThrowError = true
        fakeApiService.errorMessage = expectedExceptionMessage

        // Act & Assert
        try {
            repository.getProductos()
            fail("Se esperaba una excepción, pero no fue lanzada.")
        } catch (e: Exception) {
            assertEquals(expectedExceptionMessage, e.message)
        }
    }

    @Test
    fun `getCategorias - cuando api es exitosa - debe retornar lista de categorías`() = runTest {
        // Arrange
        fakeApiService.categoriesToReturn = sampleCategories

        // Act
        val result = repository.getCategorias()

        // Assert
        assertEquals(sampleCategories, result)
    }

    @Test
    fun `getCategorias - cuando api falla - debe propagar la excepción`() = runTest {
        // Arrange
        val expectedExceptionMessage = "Error de red"
        fakeApiService.shouldThrowError = true
        fakeApiService.errorMessage = expectedExceptionMessage

        // Act & Assert
        try {
            repository.getCategorias()
            fail("Se esperaba una excepción, pero no fue lanzada.")
        } catch (e: Exception) {
            assertEquals(expectedExceptionMessage, e.message)
        }
    }
}

class FakeCatalogApiService : CatalogApiService {
    var productsToReturn: List<ProductoDto> = emptyList()
    var categoriesToReturn: List<CategoriaDto> = emptyList()
    var shouldThrowError = false
    var errorMessage = "Error genérico"

    override suspend fun getProductos(): List<ProductoDto> {
        if (shouldThrowError) throw Exception(errorMessage)
        return productsToReturn
    }

    override suspend fun getCategorias(): List<CategoriaDto> {
        if (shouldThrowError) throw Exception(errorMessage)
        return categoriesToReturn
    }

    override suspend fun getProductoById(id: Long): ProductoDto {
        if (shouldThrowError) throw Exception(errorMessage)
        return productsToReturn.first { it.idProducto == id }
    }

    override suspend fun getProductosByCategoria(idCategoria: Long): List<ProductoDto> {
        if (shouldThrowError) throw Exception(errorMessage)
        return productsToReturn.filter { it.idCategoria == idCategoria }
    }
}

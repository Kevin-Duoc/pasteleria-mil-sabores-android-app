
package com.example.pasteleriamilsabores_grupo9

import com.example.pasteleriamilsabores_grupo9.data.remote.api.CatalogApiService
import com.example.pasteleriamilsabores_grupo9.data.remote.dto.CategoriaDto
import com.example.pasteleriamilsabores_grupo9.data.remote.dto.ProductoDto
import com.example.pasteleriamilsabores_grupo9.repository.CatalogRepository
import com.example.pasteleriamilsabores_grupo9.viewmodel.ProductListUiState
import com.example.pasteleriamilsabores_grupo9.viewmodel.ProductListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: ProductListViewModel
    private lateinit var fakeApiService: FakeCatalogApiService
    private lateinit var catalogRepository: CatalogRepository

    private val sampleCategories = listOf(
        CategoriaDto(1, "Pasteles"),
        CategoriaDto(2, "Bocaditos")
    )
    private val sampleProducts = listOf(
        ProductoDto(101L, "SKU001", "Pastel de Chocolate", "", 25, "", 10, 5, 1L, "Pasteles"),
        ProductoDto(102L, "SKU002", "Pastel de Vainilla", "", 22, "", 15, 5, 1L, "Pasteles"),
        ProductoDto(201L, "SKU003", "Mini Causa", "", 3, "", 50, 10, 2L, "Bocaditos"),
        ProductoDto(202L, "SKU004", "Empanada", "", 2, "", 40, 10, 2L, "Bocaditos")
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeApiService = FakeCatalogApiService()
        catalogRepository = CatalogRepository(fakeApiService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun setupViewModel() {
        viewModel = ProductListViewModel(catalogRepository, testDispatcher)
    }

    @Test
    fun `init - cuando el repositorio es exitoso, el estado debe ser Success y los datos cargados`() = runTest {
        fakeApiService.productsToReturn = sampleProducts
        fakeApiService.categoriesToReturn = sampleCategories

        setupViewModel()
        advanceUntilIdle() // Ejecuta la corrutina de init

        assertTrue("El estado de la UI debe ser Success", viewModel.uiState.value is ProductListUiState.Success)
        assertEquals("Debe haber 2 categorías", 2, viewModel.categories.value.size)
        assertEquals("Los productos filtrados deben ser igual a todos los productos inicialmente", 4, viewModel.filteredProducts.value.size)
    }

    @Test
    fun `init - cuando el repositorio falla, el estado de la UI debe ser Error`() = runTest {
        fakeApiService.shouldThrowError = true

        setupViewModel()
        advanceUntilIdle() // Ejecuta la corrutina de init

        assertTrue("El estado de la UI debe ser Error", viewModel.uiState.value is ProductListUiState.Error)
    }

    @Test
    fun `filterByCategory - debe filtrar la lista de productos correctamente`() = runTest {
        fakeApiService.productsToReturn = sampleProducts
        setupViewModel()
        advanceUntilIdle()

        viewModel.filterByCategory(1L)

        assertEquals("Debe haber 2 productos en la categoría 'Pasteles'", 2, viewModel.filteredProducts.value.size)
        assertTrue("Todos los productos deben ser de la categoría 1", viewModel.filteredProducts.value.all { it.idCategoria == 1L })
    }

    @Test
    fun `filterByCategory - con null debe mostrar todos los productos`() = runTest {
        fakeApiService.productsToReturn = sampleProducts
        setupViewModel()
        advanceUntilIdle()
        
        viewModel.filterByCategory(1L)
        viewModel.filterByCategory(null)

        assertEquals("La lista debe mostrar todos los productos", sampleProducts.size, viewModel.filteredProducts.value.size)
    }

    @Test
    fun `filterByCategory - debe actualizar la categoría seleccionada`() = runTest {
        setupViewModel()
        advanceUntilIdle()
        val categoryIdToSelect = 2L

        viewModel.filterByCategory(categoryIdToSelect)

        assertEquals("El ID de la categoría seleccionada debe ser 2", categoryIdToSelect, viewModel.selectedCategory.value)
    }

    @Test
    fun `filterByCategory - con una categoría sin productos debe retornar una lista vacía`() = runTest {
        fakeApiService.productsToReturn = sampleProducts
        setupViewModel()
        advanceUntilIdle()
        val categoryIdWithNoProducts = 3L

        viewModel.filterByCategory(categoryIdWithNoProducts)

        assertTrue("La lista de productos filtrados debe estar vacía", viewModel.filteredProducts.value.isEmpty())
    }

    @Test
    fun `estado inicial - selectedCategory debe ser null inicialmente`() = runTest {
        setupViewModel()
        advanceUntilIdle()

        assertEquals("La categoría seleccionada inicialmente debe ser null", null, viewModel.selectedCategory.value)
    }
    
    @Test
    fun `loadCatalog - después de un error, una recarga exitosa debe actualizar el estado a Success`() = runTest {
        fakeApiService.shouldThrowError = true
        setupViewModel()
        advanceUntilIdle()
        assertTrue("El estado inicial debe ser Error", viewModel.uiState.value is ProductListUiState.Error)

        fakeApiService.shouldThrowError = false
        fakeApiService.productsToReturn = sampleProducts
        viewModel.loadCatalog()
        advanceUntilIdle()
        
        assertTrue("El estado después de recargar debe ser Success", viewModel.uiState.value is ProductListUiState.Success)
        assertEquals("Los productos deben cargarse después de la recarga", sampleProducts.size, viewModel.filteredProducts.value.size)
    }

    @Test
    fun `uiState - el estado Success debe contener la lista de productos correcta`() = runTest {
        fakeApiService.productsToReturn = sampleProducts
        setupViewModel()
        advanceUntilIdle()
        
        val state = viewModel.uiState.value
        assertTrue(state is ProductListUiState.Success)
        assertEquals(sampleProducts, (state as ProductListUiState.Success).products)
    }

    @Test
    fun `loadCatalog - cuando el repositorio devuelve listas vacías, el estado es Success con data vacía`() = runTest {
        fakeApiService.productsToReturn = emptyList()
        fakeApiService.categoriesToReturn = emptyList()

        setupViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is ProductListUiState.Success)
        assertTrue((state as ProductListUiState.Success).products.isEmpty())
        assertTrue(viewModel.categories.value.isEmpty())
        assertTrue(viewModel.filteredProducts.value.isEmpty())
    }
}

class FakeCatalogApiService : CatalogApiService {
    var productsToReturn: List<ProductoDto> = emptyList()
    var categoriesToReturn: List<CategoriaDto> = emptyList()
    var shouldThrowError = false

    override suspend fun getProductos(): List<ProductoDto> {
        if (shouldThrowError) throw Exception("Network error")
        return productsToReturn
    }

    override suspend fun getCategorias(): List<CategoriaDto> {
        if (shouldThrowError) throw Exception("Network error")
        return categoriesToReturn
    }

    override suspend fun getProductoById(id: Long): ProductoDto {
        return productsToReturn.first { it.idProducto == id }
    }

    override suspend fun getProductosByCategoria(idCategoria: Long): List<ProductoDto> {
        return productsToReturn.filter { it.idCategoria == idCategoria }
    }
}

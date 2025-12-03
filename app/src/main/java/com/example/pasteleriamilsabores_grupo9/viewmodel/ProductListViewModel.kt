package com.example.pasteleriamilsabores_grupo9.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilsabores_grupo9.data.remote.dto.CategoriaDto
import com.example.pasteleriamilsabores_grupo9.data.remote.dto.ProductoDto
import com.example.pasteleriamilsabores_grupo9.repository.CatalogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ProductListUiState {
    data class Success(val products: List<ProductoDto>) : ProductListUiState
    object Error : ProductListUiState
    object Loading : ProductListUiState
}

class ProductListViewModel(private val catalogRepository: CatalogRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductListUiState>(ProductListUiState.Loading)
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    private val _allProducts = MutableStateFlow<List<ProductoDto>>(emptyList())
    private val _filteredProducts = MutableStateFlow<List<ProductoDto>>(emptyList())
    val filteredProducts: StateFlow<List<ProductoDto>> = _filteredProducts.asStateFlow()

    private val _categories = MutableStateFlow<List<CategoriaDto>>(emptyList())
    val categories: StateFlow<List<CategoriaDto>> = _categories.asStateFlow()

    private val _selectedCategory = MutableStateFlow<Long?>(null)
    val selectedCategory: StateFlow<Long?> = _selectedCategory.asStateFlow()

    init {
        loadCatalog()
    }

    fun loadCatalog() {
        viewModelScope.launch {
            _uiState.value = ProductListUiState.Loading
            try {
                val products = catalogRepository.getProductos()
                val categories = catalogRepository.getCategorias()
                _allProducts.value = products
                _filteredProducts.value = products
                _categories.value = categories
                _uiState.value = ProductListUiState.Success(products)
            } catch (e: Exception) {
                _uiState.value = ProductListUiState.Error
            }
        }
    }

    fun filterByCategory(categoryId: Long?) {
        _selectedCategory.value = categoryId
        _filteredProducts.value = if (categoryId == null) {
            _allProducts.value
        } else {
            _allProducts.value.filter { it.idCategoria == categoryId }
        }
    }
}

class ProductListViewModelFactory(
    private val repository: CatalogRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

package com.example.pasteleriamilsabores_grupo9.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilsabores_grupo9.data.db.entity.CarritoItem
import com.example.pasteleriamilsabores_grupo9.data.remote.dto.ProductoDto
import com.example.pasteleriamilsabores_grupo9.repository.CarritoRepository
import com.example.pasteleriamilsabores_grupo9.repository.CatalogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface ProductDetailUiState {
    data class Success(val product: ProductoDto) : ProductDetailUiState
    data class Error(val message: String) : ProductDetailUiState
    object Loading : ProductDetailUiState
}

class ProductDetailViewModel(
    private val catalogRepository: CatalogRepository,
    private val carritoRepository: CarritoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Loading)
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    private val _showAddedToCartMessage = MutableStateFlow(false)
    val showAddedToCartMessage: StateFlow<Boolean> = _showAddedToCartMessage.asStateFlow()

    fun loadProductById(productId: Long) {
        viewModelScope.launch {
            _uiState.value = ProductDetailUiState.Loading
            try {
                val product = catalogRepository.getProductoById(productId)
                _uiState.value = ProductDetailUiState.Success(product)
            } catch (e: Exception) {
                _uiState.value = ProductDetailUiState.Error("No se pudo cargar el producto.")
            }
        }
    }

    fun onAddToCartClicked(product: ProductoDto, quantity: Int) {
        viewModelScope.launch {
            val item = CarritoItem(
                productId = product.idProducto,
                nombre = product.nombre,
                precio = product.precio,
                imagenUrl = product.urlImagen,
                cantidad = quantity
            )
            carritoRepository.addToCart(item)
            _showAddedToCartMessage.value = true
        }
    }

    fun messageShown() {
        _showAddedToCartMessage.value = false
    }
}

class ProductDetailViewModelFactory(
    private val catalogRepository: CatalogRepository,
    private val carritoRepository: CarritoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductDetailViewModel(catalogRepository, carritoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

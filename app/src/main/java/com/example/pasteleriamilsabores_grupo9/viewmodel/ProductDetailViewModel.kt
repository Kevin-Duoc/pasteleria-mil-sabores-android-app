package com.example.pasteleriamilsabores_grupo9.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilsabores_grupo9.data.model.Producto
import com.example.pasteleriamilsabores_grupo9.data.remote.dto.ProductoDto
import com.example.pasteleriamilsabores_grupo9.repository.CarritoRepository
import com.example.pasteleriamilsabores_grupo9.repository.CatalogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
                _uiState.value = ProductDetailUiState.Error(e.message ?: "Error al cargar el producto")
            }
        }
    }

    fun onAddToCartClicked(productoDto: ProductoDto, cantidad: Int) {
        viewModelScope.launch {
            // Convertimos el DTO a la entidad de la base de datos local
            val producto = Producto(
                id = productoDto.codigoSku, // Usamos SKU como ID local único
                nombre = productoDto.nombre,
                descripcion = productoDto.descripcion,
                precio = productoDto.precio,
                imagenResIdName = productoDto.urlImagen,
                stock = productoDto.stock,
                stockCritico = productoDto.stockCritico
            )
            carritoRepository.addItemToCart(producto, cantidad)
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

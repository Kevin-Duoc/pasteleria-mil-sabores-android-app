package com.example.pasteleriamilsabores_grupo9.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilsabores_grupo9.data.model.Producto
import com.example.pasteleriamilsabores_grupo9.repository.CarritoRepository
import com.example.pasteleriamilsabores_grupo9.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val productoRepository: ProductoRepository,
    private val carritoRepository: CarritoRepository
) : ViewModel() {

    private val _product = MutableStateFlow<Producto?>(null)
    val product: StateFlow<Producto?> = _product.asStateFlow()

    private val _showAddedToCartMessage = MutableStateFlow(false)
    val showAddedToCartMessage: StateFlow<Boolean> = _showAddedToCartMessage.asStateFlow()
    fun loadProductById(productId: String) {
        viewModelScope.launch {
            productoRepository.getProductoById(productId).collectLatest { productoEncontrado ->
                _product.value = productoEncontrado
            }
        }
    }

    fun onAddToCartClicked(producto: Producto, cantidad: Int) {
        viewModelScope.launch {
            carritoRepository.addItemToCart(producto, cantidad)
            _showAddedToCartMessage.value = true
        }
    }

    fun messageShown() {
        _showAddedToCartMessage.value = false
    }
}

class ProductDetailViewModelFactory(
    private val productoRepository: ProductoRepository,
    private val carritoRepository: CarritoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductDetailViewModel(productoRepository, carritoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
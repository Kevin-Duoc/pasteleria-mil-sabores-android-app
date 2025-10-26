package com.example.pasteleriamilsabores_grupo9.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilsabores_grupo9.data.model.Producto
import com.example.pasteleriamilsabores_grupo9.repository.CarritoRepository // <-- 1. AÑADIR IMPORT
import com.example.pasteleriamilsabores_grupo9.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// --- 3. El ViewModel ahora PIDE AMBOS Repositorios en su constructor ---
class ProductDetailViewModel(
    private val productoRepository: ProductoRepository,
    private val carritoRepository: CarritoRepository // <-- 2. AÑADIR REPO DE CARRITO
) : ViewModel() {

    // Estado para guardar el producto encontrado (puede ser nulo si no se encuentra)
    private val _product = MutableStateFlow<Producto?>(null)
    val product: StateFlow<Producto?> = _product.asStateFlow()

    // --- 4. Función 'loadProductById' (Se queda igual) ---
    fun loadProductById(productId: String) {
        viewModelScope.launch {
            productoRepository.getProductoById(productId).collectLatest { productoEncontrado ->
                _product.value = productoEncontrado
            }
        }
    }

    // --- 5. AÑADIR NUEVA FUNCIÓN para el botón del carrito ---
    /**
     * Esta función será llamada desde la UI (la pantalla).
     * Lanza una corutina y le pide al 'carritoRepository' que
     * añada el producto con la cantidad seleccionada.
     */
    fun onAddToCartClicked(producto: Producto, cantidad: Int) {
        viewModelScope.launch {
            carritoRepository.addItemToCart(producto, cantidad)
            // (Opcional) Aquí podríamos añadir un 'StateFlow' para
            // mostrar un mensaje de "¡Añadido!" en la pantalla.
        }
    }
}

// --- 6. ACTUALIZAR LA FÁBRICA (FACTORY) ---
/**
 * La Fábrica ahora necesita AMBOS repositorios
 * para poder construir el ProductDetailViewModel.
 */
class ProductDetailViewModelFactory(
    private val productoRepository: ProductoRepository,
    private val carritoRepository: CarritoRepository // <-- 7. AÑADIR REPO DE CARRITO
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // 8. Pasamos AMBOS repositorios al constructor del ViewModel
            return ProductDetailViewModel(productoRepository, carritoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
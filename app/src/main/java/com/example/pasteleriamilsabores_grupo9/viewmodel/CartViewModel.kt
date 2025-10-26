package com.example.pasteleriamilsabores_grupo9.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilsabores_grupo9.data.model.ItemCarrito
import com.example.pasteleriamilsabores_grupo9.repository.CarritoRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(private val carritoRepository: CarritoRepository) : ViewModel() {

    // 1. Conectamos el 'Flow' de todos los items del carrito a un 'StateFlow'
    //    que la UI (CartScreen) podrá observar.
    val cartItems: StateFlow<List<ItemCarrito>> = carritoRepository.allCartItems
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList() // Empezamos con una lista vacía
        )

    // 2. Función para actualizar la cantidad de un item
    fun updateItemQuantity(item: ItemCarrito, newQuantity: Int) {
        viewModelScope.launch {
            if (newQuantity > 0) {
                // Creamos una copia del item con la nueva cantidad y la actualizamos
                carritoRepository.updateItem(item.copy(cantidad = newQuantity))
            } else {
                // Si la cantidad es 0 o menos, eliminamos el item
                carritoRepository.deleteItem(item)
            }
        }
    }

    // 3. Función para eliminar un item directamente
    fun deleteItem(item: ItemCarrito) {
        viewModelScope.launch {
            carritoRepository.deleteItem(item)
        }
    }
}

/**
 * Fábrica (Factory) para crear el CartViewModel.
 * Es necesaria porque nuestro ViewModel pide un 'carritoRepository'
 * en su constructor.
 */
class CartViewModelFactory(
    private val carritoRepository: CarritoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CartViewModel(carritoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
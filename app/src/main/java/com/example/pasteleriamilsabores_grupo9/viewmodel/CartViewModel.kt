package com.example.pasteleriamilsabores_grupo9.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilsabores_grupo9.data.model.ItemCarrito
import com.example.pasteleriamilsabores_grupo9.repository.AuthRepository
import com.example.pasteleriamilsabores_grupo9.repository.CarritoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(
    private val carritoRepository: CarritoRepository,
    private val authRepository: AuthRepository // <-- 2. AÑADIR AuthRepository
) : ViewModel() {

    // 1. Estado de los items del carrito (se queda igual)
    val cartItems: StateFlow<List<ItemCarrito>> = carritoRepository.allCartItems
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // --- 3. NUEVO ESTADO: ¿Está el usuario conectado? ---
    /**
     * Observa el 'currentUser' del AuthRepository y lo transforma
     * en un simple Boolean (true si hay usuario, false si no).
     * La UI observará ESTE estado para saber si habilitar el botón "Pagar".
     */
    val isUserLoggedIn: StateFlow<Boolean> = authRepository.currentUser
        .map { it != null } // Transforma Usuario? a Boolean
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false // Asumimos que no está logueado al principio
        )

    // (Las funciones updateItemQuantity y deleteItem se quedan igual)
    fun updateItemQuantity(item: ItemCarrito, newQuantity: Int) {
        viewModelScope.launch {
            if (newQuantity > 0) {
                carritoRepository.updateItem(item.copy(cantidad = newQuantity))
            } else {
                carritoRepository.deleteItem(item)
            }
        }
    }

    fun deleteItem(item: ItemCarrito) {
        viewModelScope.launch {
            carritoRepository.deleteItem(item)
        }
    }
}

/**
 * Fábrica (Factory) para crear el CartViewModel.
 * Ahora necesita AMBOS repositorios.
 */
class CartViewModelFactory(
    private val carritoRepository: CarritoRepository,
    private val authRepository: AuthRepository // <-- 4. AÑADIR AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // 5. Pasamos AMBOS repositorios al constructor del ViewModel
            return CartViewModel(carritoRepository, authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
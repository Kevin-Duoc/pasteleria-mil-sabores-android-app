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
    private val authRepository: AuthRepository
) : ViewModel() {

    val cartItems: StateFlow<List<ItemCarrito>> = carritoRepository.allCartItems
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val isUserLoggedIn: StateFlow<Boolean> = authRepository.currentUser
        .map { it != null }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

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

class CartViewModelFactory(
    private val carritoRepository: CarritoRepository,
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CartViewModel(carritoRepository, authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
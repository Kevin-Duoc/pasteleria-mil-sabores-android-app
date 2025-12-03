package com.example.pasteleriamilsabores_grupo9.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilsabores_grupo9.data.db.entity.CarritoItem
import com.example.pasteleriamilsabores_grupo9.repository.AuthRepository
import com.example.pasteleriamilsabores_grupo9.repository.CarritoRepository
import com.example.pasteleriamilsabores_grupo9.repository.PedidoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Define los posibles estados del proceso de checkout
sealed interface CheckoutUiState {
    object Idle : CheckoutUiState      // Estado inicial, no ha pasado nada
    object Loading : CheckoutUiState   // El pedido se está enviando
    data class Success(val message: String) : CheckoutUiState // El pedido se creó con éxito
    data class Error(val message: String) : CheckoutUiState   // Ocurrió un error
}

class CartViewModel(
    private val carritoRepository: CarritoRepository,
    private val authRepository: AuthRepository,
    private val pedidoRepository: PedidoRepository // Inyectamos el nuevo repositorio
) : ViewModel() {

    val cartItems: StateFlow<List<CarritoItem>> = carritoRepository.allItems
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

    // StateFlow para comunicar el estado del checkout a la UI
    private val _checkoutState = MutableStateFlow<CheckoutUiState>(CheckoutUiState.Idle)
    val checkoutState: StateFlow<CheckoutUiState> = _checkoutState.asStateFlow()

    fun onCheckoutClicked() {
        viewModelScope.launch {
            val currentUser = authRepository.currentUser.first() // Obtenemos el usuario actual
            val currentCartItems = cartItems.value

            // Validaciones
            if (currentUser == null) {
                _checkoutState.value = CheckoutUiState.Error("Debes iniciar sesión para continuar.")
                return@launch
            }
            if (currentCartItems.isEmpty()) {
                _checkoutState.value = CheckoutUiState.Error("Tu carrito está vacío.")
                return@launch
            }

            _checkoutState.value = CheckoutUiState.Loading // Empezamos a cargar

            // Llamamos al repositorio para crear el pedido, convirtiendo el ID a Long
            val success = pedidoRepository.crearPedido(currentCartItems, currentUser.id.toLong())

            if (success) {
                carritoRepository.clearCart() // Si es exitoso, vaciamos el carrito local
                _checkoutState.value = CheckoutUiState.Success("¡Pedido creado con éxito!")
            } else {
                _checkoutState.value = CheckoutUiState.Error("No se pudo crear el pedido. Intente nuevamente.")
            }
        }
    }

    fun updateItemQuantity(item: CarritoItem, newQuantity: Int) {
        viewModelScope.launch {
            if (newQuantity > 0) {
                carritoRepository.update(item.copy(cantidad = newQuantity))
            } else {
                carritoRepository.delete(item)
            }
        }
    }

    fun deleteItem(item: CarritoItem) {
        viewModelScope.launch {
            carritoRepository.delete(item)
        }
    }

    // Función para resetear el estado y poder cerrar los diálogos/mensajes
    fun resetCheckoutState() {
        _checkoutState.value = CheckoutUiState.Idle
    }
}

class CartViewModelFactory(
    private val carritoRepository: CarritoRepository,
    private val authRepository: AuthRepository,
    private val pedidoRepository: PedidoRepository // Añadimos el nuevo repositorio a la Factory
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CartViewModel(carritoRepository, authRepository, pedidoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

package com.example.pasteleriamilsabores_grupo9.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilsabores_grupo9.data.remote.dto.pedidos.PedidoResponseDto
import com.example.pasteleriamilsabores_grupo9.repository.PedidoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Define los posibles estados de la pantalla "Mis Pedidos"
 */
sealed interface MisPedidosUiState {
    object Loading : MisPedidosUiState
    data class Success(val pedidos: List<PedidoResponseDto>) : MisPedidosUiState
    data class Error(val message: String) : MisPedidosUiState
}

/**
 * ViewModel para la pantalla "Mis Pedidos".
 */
class MisPedidosViewModel(
    private val pedidoRepository: PedidoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MisPedidosUiState>(MisPedidosUiState.Loading)
    val uiState: StateFlow<MisPedidosUiState> = _uiState.asStateFlow()

    init {
        // Carga los pedidos en cuanto el ViewModel se crea.
        loadPedidos()
    }

    fun loadPedidos() {
        viewModelScope.launch {
            _uiState.value = MisPedidosUiState.Loading
            try {
                // Llama al repositorio para obtener la lista de pedidos.
                val pedidos = pedidoRepository.getPedidos()
                _uiState.value = MisPedidosUiState.Success(pedidos)
            } catch (e: Exception) {
                _uiState.value = MisPedidosUiState.Error("No se pudieron cargar los pedidos.")
            }
        }
    }
}

/**
 * Factory para crear una instancia de MisPedidosViewModel.
 */
class MisPedidosViewModelFactory(
    private val pedidoRepository: PedidoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MisPedidosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MisPedidosViewModel(pedidoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

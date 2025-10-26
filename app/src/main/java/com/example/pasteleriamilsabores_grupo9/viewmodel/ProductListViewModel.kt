package com.example.pasteleriamilsabores_grupo9.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilsabores_grupo9.data.model.Producto
import com.example.pasteleriamilsabores_grupo9.repository.ProductoRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ProductListViewModel(repository: ProductoRepository) : ViewModel() {

    // --- 4. Conectamos el Flow del Repositorio directamente a la UI ---
    /**
     * Esta es la parte genial. Ya NO necesitamos un 'MutableStateFlow' privado
     * ni una función 'loadProducts()'.
     *
     * 'repository.allProductos' es el 'Flow' que viene del DAO.
     * Usamos '.stateIn' para convertir ese "chorro" de datos (Flow)
     * en un 'StateFlow' (un estado) que la UI pueda observar.
     *
     * Se actualizará solo, automáticamente, si algo cambia en la BD.
     */
    val products: StateFlow<List<Producto>> = repository.allProductos
        .stateIn(
            scope = viewModelScope, // El 'scope' de este ViewModel
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000), // Cuándo empezar a "escuchar"
            initialValue = emptyList() // Qué mostrar mientras se cargan los datos
        )
}


// --- 5. AÑADIMOS UNA FÁBRICA (FACTORY) ---
/**
 * ¿Por qué esto?
 * Porque nuestro ViewModel ya no tiene un constructor vacío (ahora pide un 'repository').
 * Android no sabe cómo crear este ViewModel por sí solo.
 *
 * Esta "Fábrica" es una clase que le dice a Android:
 * "Oye, para crear un 'ProductListViewModel', toma este 'repository' que te paso
 * y úsalo en su constructor".
 */
class ProductListViewModelFactory(
    private val repository: ProductoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
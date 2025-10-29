package com.example.pasteleriamilsabores_grupo9.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilsabores_grupo9.data.model.Producto
import com.example.pasteleriamilsabores_grupo9.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

object Categorias {
    const val VER_TODO = "Ver Todo"
    const val TORTAS = "Tortas"
    const val POSTRES = "Postres"
    const val SIN_AZUCAR = "Sin Azúcar"
    const val SIN_GLUTEN = "Sin Gluten"
    const val VEGANO = "Vegano"
}

class ProductListViewModel(repository: ProductoRepository) : ViewModel() {

    private val _selectedCategories = MutableStateFlow<Set<String>>(setOf(Categorias.VER_TODO))
    val selectedCategories: StateFlow<Set<String>> = _selectedCategories.asStateFlow()

    private val _maxPrice = MutableStateFlow(60000f)
    val maxPrice: StateFlow<Float> = _maxPrice.asStateFlow()

    // --- LISTA FILTRADA (LÓGICA "O" - OR) ---
    val products: StateFlow<List<Producto>> = combine(
        repository.allProductos,
        _selectedCategories,
        _maxPrice
    ) { allProducts, categories, price ->

        val priceFiltered = allProducts.filter { it.precio <= price }

        if (categories.contains(Categorias.VER_TODO) || categories.isEmpty()) {
            priceFiltered
        } else {
            priceFiltered.filter { producto ->
                categories.any { category ->
                    when (category) {
                        Categorias.TORTAS -> producto.id.startsWith("TC") || producto.id.startsWith("TT") || producto.id.startsWith("TE") || producto.id.startsWith("PSA") || producto.id.startsWith("PV")
                        Categorias.POSTRES -> producto.id.startsWith("PI") || producto.id.startsWith("PT") || producto.id.startsWith("PG")
                        Categorias.SIN_AZUCAR -> producto.id.startsWith("PSA")
                        Categorias.SIN_GLUTEN -> producto.id.startsWith("PG")
                        Categorias.VEGANO -> producto.id.startsWith("PV")
                        else -> false
                    }
                }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun updatePrice(newPrice: Float) {
        _maxPrice.value = newPrice
    }

    fun updateCategory(category: String, isSelected: Boolean) {
        _selectedCategories.value = _selectedCategories.value.toMutableSet().apply {
            if (category == Categorias.VER_TODO) {
                clear()
                add(Categorias.VER_TODO)
            } else {
                if (isSelected) {
                    add(category)
                    remove(Categorias.VER_TODO)
                } else {
                    remove(category)
                }
                if (isEmpty()) {
                    add(Categorias.VER_TODO)
                }
            }
        }
    }
}

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
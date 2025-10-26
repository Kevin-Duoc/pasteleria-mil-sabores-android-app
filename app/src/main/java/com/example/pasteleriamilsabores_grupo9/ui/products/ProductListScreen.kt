package com.example.pasteleriamilsabores_grupo9.ui.products

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pasteleriamilsabores_grupo9.PasteleriaApplication // <-- 1. IMPORTAMOS LA APPLICATION
import com.example.pasteleriamilsabores_grupo9.data.model.Producto
import com.example.pasteleriamilsabores_grupo9.ui.theme.PasteleriaMilSabores_Grupo9Theme
import com.example.pasteleriamilsabores_grupo9.viewmodel.ProductListViewModel
import com.example.pasteleriamilsabores_grupo9.viewmodel.ProductListViewModelFactory // <-- 2. IMPORTAMOS LA FACTORY

/**
 * Esta es la pantalla "inteligente" (Smart Composable).
 * Su ÚNICA responsabilidad es obtener los datos (el ViewModel)
 * y pasárselos a la pantalla "tonta" que solo dibuja la UI.
 */
@Composable
fun ProductListScreen(
    navController: NavController,
) {
    // --- 3. INYECCIÓN DE DEPENDENCIA MANUAL ---
    // Obtenemos el contexto actual
    val context = LocalContext.current
    // Creamos la Fábrica, pasándole el repositorio desde nuestra Application
    val factory = ProductListViewModelFactory(
        (context.applicationContext as PasteleriaApplication).productoRepository
    )
    // 4. Inyectamos la fábrica en la función viewModel()
    val productListViewModel: ProductListViewModel = viewModel(factory = factory)

    // Observamos el estado 'products' del ViewModel
    val products by productListViewModel.products.collectAsState()

    // 5. Llamamos al Composable "tonto" que solo dibuja la UI
    ProductListContent(products = products, navController = navController)
}

/**
 * Esta es la pantalla "tonta" (Dumb Composable).
 * No sabe de dónde vienen los datos, solo los recibe y los dibuja.
 * Esto hace que sea fácil de probar y previsualizar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListContent(
    products: List<Producto>,
    navController: NavController
) {
    // Este Scaffold y LazyVerticalGrid es el mismo código que tenías
    Scaffold(topBar = { TopAppBar(title = { Text("Productos") }) }) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(products) { producto ->
                ProductCard(producto = producto, onClick = {
                    navController.navigate("product_detail/${producto.id}")
                })
            }
        }
    }
}

// Composable para mostrar una tarjeta de producto individual
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(producto: Producto, onClick: () -> Unit) {
    val context = LocalContext.current

    @Composable
    fun getDrawableResourceId(name: String): Int {
        return remember(name) {
            context.resources.getIdentifier(name.lowercase(), "drawable", context.packageName)
        }
    }

    val imageResId = getDrawableResourceId(producto.imagenResIdName)
    val finalImageResId = if (imageResId == 0) getDrawableResourceId("placeholder_image") else imageResId

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = if (finalImageResId != 0) finalImageResId else android.R.drawable.ic_menu_gallery),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "CLP $${producto.precio.formatPrice()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// Función de extensión para formatear el precio
fun Int.formatPrice(): String {
    return this.toString().reversed().chunked(3).joinToString(".").reversed()
}


@Preview(showBackground = true)
@Composable
fun ProductListScreenPreview() {
    // --- 6. PREVIEW ARREGLADA ---
    // 1. Creamos una lista falsa SOLO para la preview
    val fakeProducts = listOf(
        Producto("P1", "Torta de Chocolate", "Desc", 10000, "torta_cuadrada_chocolate", 5, 2),
        Producto("P2", "Torta de Vainilla", "Desc", 20000, "torta_circular_vainilla", 5, 2)
    )

    PasteleriaMilSabores_Grupo9Theme {
        // 2. Llamamos al Composable "tonto" con la lista falsa
        ProductListContent(products = fakeProducts, navController = rememberNavController())
    }
}
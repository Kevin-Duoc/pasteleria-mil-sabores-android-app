package com.example.pasteleriamilsabores_grupo9.ui.products

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pasteleriamilsabores_grupo9.R
import com.example.pasteleriamilsabores_grupo9.PasteleriaApplication
import com.example.pasteleriamilsabores_grupo9.data.remote.dto.ProductoDto
import com.example.pasteleriamilsabores_grupo9.ui.theme.PasteleriaMilSabores_Grupo9Theme
import com.example.pasteleriamilsabores_grupo9.viewmodel.ProductListUiState
import com.example.pasteleriamilsabores_grupo9.viewmodel.ProductListViewModel
import com.example.pasteleriamilsabores_grupo9.viewmodel.ProductListViewModelFactory

@Composable
fun ProductListScreen(
    navController: NavController,
    productListViewModel: ProductListViewModel
) {
    val uiState by productListViewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            is ProductListUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is ProductListUiState.Error -> {
                ConnectionErrorScreen(onRetry = { productListViewModel.loadCatalog() })
            }
            is ProductListUiState.Success -> {
                val filteredProducts by productListViewModel.filteredProducts.collectAsState()
                ProductListContent(
                    products = filteredProducts,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun ConnectionErrorScreen(onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Error Icon",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "¡Ups! Algo salió mal",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "No pudimos conectar con el catálogo de Mil Sabores. Intenta más tarde.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Reintentar")
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListContent(
    products: List<ProductoDto>,
    navController: NavController
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        modifier = Modifier.fillMaxSize().padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 8.dp)
    ) {
        items(products, key = { it.idProducto }) { producto ->
            ProductCard(producto = producto, onClick = {
                navController.navigate("product_detail/${producto.idProducto}")
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(producto: ProductoDto, onClick: () -> Unit) {
    val context = LocalContext.current
    val imageName = producto.urlImagen.substringBeforeLast('.')
    val imageResId = remember(imageName) {
        context.resources.getIdentifier(imageName.lowercase(), "drawable", context.packageName)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = if (imageResId != 0) imageResId else R.drawable.placeholder_image),
                contentDescription = producto.nombre,
                modifier = Modifier.fillMaxWidth().height(120.dp),
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

fun Int.formatPrice(): String {
    return try {
        this.toString().reversed().chunked(3).joinToString(".").reversed()
    } catch (e: Exception) { this.toString() }
}

@Preview(showBackground = true)
@Composable
fun ProductListScreenPreview() {
    val fakeProductsPreview = listOf(
        ProductoDto(1L, "P1", "Torta de Chocolate", "Desc", 10000, "torta_cuadrada_chocolate.jpg", 5, 2, 1L, "Tortas"),
        ProductoDto(2L, "P2", "Torta de Vainilla", "Desc", 20000, "torta_circular_vainilla.jpg", 5, 2, 1L, "Tortas")
    )

    PasteleriaMilSabores_Grupo9Theme {
        ProductListContent(
            products = fakeProductsPreview,
            navController = rememberNavController()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ConnectionErrorScreenPreview() {
    PasteleriaMilSabores_Grupo9Theme {
        ConnectionErrorScreen(onRetry = {})
    }
}

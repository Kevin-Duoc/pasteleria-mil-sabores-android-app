package com.example.pasteleriamilsabores_grupo9.ui.products

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pasteleriamilsabores_grupo9.PasteleriaApplication
import com.example.pasteleriamilsabores_grupo9.data.model.Producto
import com.example.pasteleriamilsabores_grupo9.ui.theme.PasteleriaMilSabores_Grupo9Theme
import com.example.pasteleriamilsabores_grupo9.viewmodel.ProductListViewModel
import com.example.pasteleriamilsabores_grupo9.viewmodel.ProductListViewModelFactory

@Composable
fun ProductListScreen(
    navController: NavController,
    productListViewModel: ProductListViewModel
) {
    val products by productListViewModel.products.collectAsState()
    ProductListContent(products = products, navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListContent(
    products: List<Producto>,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = "Productos",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, start = 8.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 8.dp)
        ) {
            items(products) { producto ->
                ProductCard(producto = producto, onClick = {
                    navController.navigate("product_detail/${producto.id}")
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(producto: Producto, onClick: () -> Unit) {
    val context = LocalContext.current
    @Composable
    fun getDrawableResourceId(name: String): Int {
        return remember(name) {
            try {
                context.resources.getIdentifier(
                    name.lowercase(),
                    "drawable",
                    context.packageName.takeIf { it.isNotEmpty() } ?: context.packageName
                )
            } catch (e: Exception) { 0 }
        }
    }
    val imageResId = getDrawableResourceId(producto.imagenResIdName)
    val placeholderResId = getDrawableResourceId("placeholder_image")
    val finalImageResId = if (imageResId != 0) imageResId else if (placeholderResId != 0) placeholderResId else android.R.drawable.ic_menu_gallery

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = finalImageResId),
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
        Producto("P1", "Torta de Chocolate", "Desc", 10000, "torta_cuadrada_chocolate", 5, 2),
        Producto("P2", "Torta de Vainilla", "Desc", 20000, "torta_circular_vainilla", 5, 2)
    )
    PasteleriaMilSabores_Grupo9Theme {
        ProductListContent(products = fakeProductsPreview, navController = rememberNavController())
    }
}

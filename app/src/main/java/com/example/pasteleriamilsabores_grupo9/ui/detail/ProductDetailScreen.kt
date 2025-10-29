package com.example.pasteleriamilsabores_grupo9.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pasteleriamilsabores_grupo9.PasteleriaApplication
import com.example.pasteleriamilsabores_grupo9.data.model.Producto
import com.example.pasteleriamilsabores_grupo9.ui.theme.PasteleriaMilSabores_Grupo9Theme
import com.example.pasteleriamilsabores_grupo9.viewmodel.ProductDetailViewModel
import com.example.pasteleriamilsabores_grupo9.viewmodel.ProductDetailViewModelFactory

@Composable
fun ProductDetailScreen(
    navController: NavController,
    productId: String?,
) {
    val context = LocalContext.current
    val application = context.applicationContext as PasteleriaApplication
    val factory = ProductDetailViewModelFactory(
        application.productoRepository,
        application.carritoRepository
    )

    val productDetailViewModel: ProductDetailViewModel = viewModel(factory = factory)

    val product by productDetailViewModel.product.collectAsState()

    LaunchedEffect(productId) {
        if (productId != null) {
            productDetailViewModel.loadProductById(productId)
        }
    }

    ProductDetailContent(
        product = product,
        navController = navController,
        productId = productId,
        onAddToCart = { p, q ->
            productDetailViewModel.onAddToCartClicked(p, q)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailContent(
    product: Producto?,
    navController: NavController,
    productId: String?,
    onAddToCart: (Producto, Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product?.nombre ?: "Detalle del Producto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            product?.let { p ->
                val context = LocalContext.current
                val imageResId = remember(p.imagenResIdName) {
                    context.resources.getIdentifier(p.imagenResIdName.lowercase(), "drawable", context.packageName)
                }
                val placeholderResId = remember {
                    context.resources.getIdentifier("placeholder_image", "drawable", context.packageName)
                }
                val finalImageResId = if (imageResId == 0) placeholderResId else imageResId

                Image(
                    painter = painterResource(id = if (finalImageResId != 0) finalImageResId else android.R.drawable.ic_menu_gallery),
                    contentDescription = p.nombre,
                    modifier = Modifier.fillMaxWidth().height(250.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(p.nombre, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "CLP $${p.precio.formatPrice()}",
                    style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.primary)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Descripción:", style = MaterialTheme.typography.titleMedium)
                Text(p.descripcion, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Stock disponible: ${p.stock}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))

                var quantity by remember { mutableStateOf(1) }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = { if (quantity > 1) quantity-- },
                        enabled = quantity > 1
                    ) { Text("-") }
                    Text(
                        text = "$quantity",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    OutlinedButton(
                        onClick = { if (quantity < p.stock) quantity++ },
                        enabled = quantity < p.stock
                    ) { Text("+") }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        onAddToCart(p, quantity)
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = p.stock > 0
                ) {
                    Text(
                        text = if (p.stock > 0) "Añadir al Carrito" else "Agotado",
                        fontSize = 18.sp
                    )
                }

            } ?: run {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    if (productId == null) {
                        Text("Error: No se especificó un ID de producto.")
                    } else {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

fun Int.formatPrice(): String {
    return this.toString().reversed().chunked(3).joinToString(".").reversed()
}


@Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    val fakeProduct = Producto(
        id = "P1",
        nombre = "Torta de Chocolate (Preview)",
        descripcion = "Descripción de la torta de prueba...",
        precio = 45000,
        imagenResIdName = "torta_cuadrada_chocolate",
        stock = 10,
        stockCritico = 5
    )
    PasteleriaMilSabores_Grupo9Theme {
        ProductDetailContent(
            product = fakeProduct,
            navController = rememberNavController(),
            productId = fakeProduct.id,
            onAddToCart = { _, _ -> }
        )
    }
}
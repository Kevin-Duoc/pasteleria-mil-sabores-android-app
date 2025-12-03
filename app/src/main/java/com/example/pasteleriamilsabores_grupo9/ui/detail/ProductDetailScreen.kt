package com.example.pasteleriamilsabores_grupo9.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pasteleriamilsabores_grupo9.R
import com.example.pasteleriamilsabores_grupo9.PasteleriaApplication
import com.example.pasteleriamilsabores_grupo9.data.remote.dto.ProductoDto
import com.example.pasteleriamilsabores_grupo9.ui.theme.PasteleriaMilSabores_Grupo9Theme
import com.example.pasteleriamilsabores_grupo9.viewmodel.ProductDetailUiState
import com.example.pasteleriamilsabores_grupo9.viewmodel.ProductDetailViewModel
import com.example.pasteleriamilsabores_grupo9.viewmodel.ProductDetailViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    navController: NavController,
    productId: Long?,
) {
    val context = LocalContext.current
    val application = context.applicationContext as PasteleriaApplication
    val factory = ProductDetailViewModelFactory(
        application.catalogRepository,
        application.carritoRepository
    )

    val productDetailViewModel: ProductDetailViewModel = viewModel(factory = factory)

    val uiState by productDetailViewModel.uiState.collectAsState()
    val showMessage by productDetailViewModel.showAddedToCartMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(productId) {
        if (productId != null) {
            productDetailViewModel.loadProductById(productId)
        }
    }

    LaunchedEffect(showMessage) {
        if (showMessage) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "¡Producto añadido al carrito!",
                    duration = SnackbarDuration.Short
                )
            }
            productDetailViewModel.messageShown()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    val topBarTitle = when (val state = uiState) {
                        is ProductDetailUiState.Success -> state.product.nombre
                        else -> "Detalle del Producto"
                    }
                    Text(topBarTitle)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (val state = uiState) {
                is ProductDetailUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is ProductDetailUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Error: ${state.message}",
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                is ProductDetailUiState.Success -> {
                    ProductDetailContent(
                        product = state.product,
                        onAddToCart = { p, q ->
                            productDetailViewModel.onAddToCartClicked(p, q)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductDetailContent(
    product: ProductoDto,
    onAddToCart: (ProductoDto, Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        val context = LocalContext.current
        val imageName = product.urlImagen.substringBeforeLast('.').lowercase()
        val imageResId = remember(imageName) {
            context.resources.getIdentifier(imageName, "drawable", context.packageName)
        }

        Image(
            painter = painterResource(id = if (imageResId != 0) imageResId else R.drawable.placeholder_image),
            contentDescription = product.nombre,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(product.nombre, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "CLP $${product.precio.formatPrice()}",
            style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.primary)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Descripción:", style = MaterialTheme.typography.titleMedium)
        Text(product.descripcion, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Stock disponible: ${product.stock}", style = MaterialTheme.typography.bodyMedium)
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
                onClick = { if (quantity < product.stock) quantity++ },
                enabled = quantity < product.stock
            ) { Text("+") }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                onAddToCart(product, quantity)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = product.stock > 0
        ) {
            Text(
                text = if (product.stock > 0) "Añadir al Carrito" else "Agotado",
                fontSize = 18.sp
            )
        }
    }
}

fun Int.formatPrice(): String {
    return this.toString().reversed().chunked(3).joinToString(".").reversed()
}

@Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    val fakeProduct = ProductoDto(
        idProducto = 1L,
        codigoSku = "P1",
        nombre = "Torta de Chocolate (Preview)",
        descripcion = "Descripción de la torta de prueba...",
        precio = 45000,
        urlImagen = "torta_cuadrada_chocolate.jpg",
        stock = 10,
        stockCritico = 5,
        idCategoria = 1L,
        nombreCategoria = "Tortas"
    )
    PasteleriaMilSabores_Grupo9Theme {
        ProductDetailContent(
            product = fakeProduct,
            onAddToCart = { _, _ -> }
        )
    }
}

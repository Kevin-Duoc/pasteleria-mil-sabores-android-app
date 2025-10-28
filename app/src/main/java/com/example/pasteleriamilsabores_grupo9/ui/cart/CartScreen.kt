package com.example.pasteleriamilsabores_grupo9.ui.cart

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pasteleriamilsabores_grupo9.PasteleriaApplication
import com.example.pasteleriamilsabores_grupo9.Routes
import com.example.pasteleriamilsabores_grupo9.data.model.ItemCarrito
import com.example.pasteleriamilsabores_grupo9.ui.theme.PasteleriaMilSabores_Grupo9Theme
import com.example.pasteleriamilsabores_grupo9.viewmodel.CartViewModel
import com.example.pasteleriamilsabores_grupo9.viewmodel.CartViewModelFactory
import kotlin.math.absoluteValue

@Composable
fun CartScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val application = context.applicationContext as PasteleriaApplication
    val factory = CartViewModelFactory(
        application.carritoRepository,
        application.authRepository
    )
    val viewModel: CartViewModel = viewModel(factory = factory)

    val cartItems by viewModel.cartItems.collectAsState()
    val isLoggedIn by viewModel.isUserLoggedIn.collectAsState()

    val total = remember(cartItems) {
        cartItems.sumOf { it.precio.absoluteValue * it.cantidad.absoluteValue } // Use absoluteValue for safety
    }

    CartContent(
        items = cartItems,
        total = total,
        isLoggedIn = isLoggedIn,
        onDeleteItem = { item -> viewModel.deleteItem(item) },
        onProceedToCheckout = {
            if (isLoggedIn) {
                Toast.makeText(context, "TODO: Ir a pantalla de pago", Toast.LENGTH_SHORT).show()
            } else {
                navController.navigate(Routes.LOGIN)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartContent(
    items: List<ItemCarrito>,
    total: Int,
    isLoggedIn: Boolean,
    onDeleteItem: (ItemCarrito) -> Unit,
    onProceedToCheckout: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (items.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("Tu carrito está vacío", style = MaterialTheme.typography.titleMedium)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items, key = { it.itemId }) { item ->
                    CartItemRow(
                        item = item,
                        onDeleteItem = { onDeleteItem(item) }
                    )
                }
            }
        }
        TotalRow(
            total = total,
            isLoggedIn = isLoggedIn,
            onProceedToCheckout = onProceedToCheckout
        )
    }
}

@Composable
fun CartItemRow(item: ItemCarrito, onDeleteItem: () -> Unit) {
    val context = LocalContext.current
    val placeholderImageResId = remember {
        try {
            context.resources.getIdentifier(
                "placeholder_image",
                "drawable",
                context.packageName.takeIf { it.isNotEmpty() } ?: context.packageName
            )
        } catch (e: Exception) {
            android.R.drawable.ic_menu_gallery
        }
    }
    val finalImageResId = if (placeholderImageResId != 0) placeholderImageResId else android.R.drawable.ic_menu_gallery

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = finalImageResId),
                contentDescription = item.nombre,
                modifier = Modifier.size(80.dp).padding(end = 16.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(item.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Cantidad: ${item.cantidad}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    "CLP $${(item.precio * item.cantidad).formatPrice()}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = onDeleteItem) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

// --- Fila del Total ---
@Composable
fun TotalRow(total: Int, isLoggedIn: Boolean, onProceedToCheckout: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = MaterialTheme.shapes.extraSmall
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Total:", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(
                    "CLP $${total.formatPrice()}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onProceedToCheckout,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = total > 0
            ) {
                Text(
                    text = if (isLoggedIn) "Ir a Pagar" else "Iniciar Sesión para Pagar",
                    fontSize = 18.sp
                )
            }
        }
    }
}

// --- Función de Formato ---
fun Int.formatPrice(): String {
    // Implementación simple, asegúrate de que maneje números grandes si es necesario
    return try {
        this.toString().reversed().chunked(3).joinToString(".").reversed()
    } catch (e: Exception) {
        this.toString() // Fallback
    }
}

private val fakeItemsPreview = listOf(
    ItemCarrito(itemId=1, productId = "P1", nombre = "Torta de Chocolate", precio = 45000, cantidad = 1),
    ItemCarrito(itemId=2, productId = "P2", nombre = "Mousse de Chocolate", precio = 5000, cantidad = 3)
)

@Preview(showBackground = true, name = "Carrito Lleno - Logueado")
@Composable
fun CartScreenPreviewLoggedIn() {
    PasteleriaMilSabores_Grupo9Theme {
        CartContent(items = fakeItemsPreview, total = 60000, isLoggedIn = true, onDeleteItem = {}, onProceedToCheckout = {})
    }
}

@Preview(showBackground = true, name = "Carrito Lleno - No Logueado")
@Composable
fun CartScreenPreviewLoggedOut() {
    PasteleriaMilSabores_Grupo9Theme {
        CartContent(items = fakeItemsPreview, total = 60000, isLoggedIn = false, onDeleteItem = {}, onProceedToCheckout = {})
    }
}

@Preview(showBackground = true, name = "Carrito Vacío")
@Composable
fun CartScreenPreviewEmpty() {
    PasteleriaMilSabores_Grupo9Theme {
        CartContent(items = emptyList(), total = 0, isLoggedIn = false, onDeleteItem = {}, onProceedToCheckout = {})
    }
}
package com.example.pasteleriamilsabores_grupo9.ui.cart

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
import com.example.pasteleriamilsabores_grupo9.PasteleriaApplication
import com.example.pasteleriamilsabores_grupo9.data.model.ItemCarrito
import com.example.pasteleriamilsabores_grupo9.ui.theme.PasteleriaMilSabores_Grupo9Theme
import com.example.pasteleriamilsabores_grupo9.viewmodel.CartViewModel
import com.example.pasteleriamilsabores_grupo9.viewmodel.CartViewModelFactory

/**
 * Pantalla "Inteligente" (Smart Composable)
 * Obtiene el ViewModel y los datos.
 */
@Composable
fun CartScreen(
    // (Opcional) Podríamos necesitar el NavController si añadimos un botón de "Pagar"
    // navController: NavController
) {
    // 1. Inyectamos el ViewModel usando la Fábrica
    val context = LocalContext.current
    val factory = CartViewModelFactory(
        (context.applicationContext as PasteleriaApplication).carritoRepository
    )
    val viewModel: CartViewModel = viewModel(factory = factory)

    // 2. Observamos la lista de items del carrito
    val cartItems by viewModel.cartItems.collectAsState()

    // 3. Calculamos el total
    // 'remember' recalcula el total solo si 'cartItems' cambia
    val total = remember(cartItems) {
        cartItems.sumOf { it.precio * it.cantidad }
    }

    // 4. Llamamos a la pantalla "tonta" que dibuja la UI
    CartContent(
        items = cartItems,
        total = total,
        onDeleteItem = { item ->
            // Le decimos al ViewModel que borre el item
            viewModel.deleteItem(item)
        }
        // (Pasaríamos funciones de update quantity aquí si tuviéramos botones +/-)
    )
}

/**
 * Pantalla "Tonta" (Dumb Composable)
 * Solo recibe datos y los dibuja.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartContent(
    items: List<ItemCarrito>,
    total: Int,
    onDeleteItem: (ItemCarrito) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (items.isEmpty()) {
            // Mensaje si el carrito está vacío
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Ocupa todo el espacio
                contentAlignment = Alignment.Center
            ) {
                Text("Tu carrito está vacío", style = MaterialTheme.typography.titleMedium)
            }
        } else {
            // Lista de items del carrito
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Ocupa todo el espacio menos el del total
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

        // Fila del Total (siempre visible abajo)
        TotalRow(total = total)
    }
}

/**
 * Dibuja una sola fila de un item en el carrito.
 */
@Composable
fun CartItemRow(item: ItemCarrito, onDeleteItem: () -> Unit) {
    val context = LocalContext.current

    // Obtenemos el ID de la imagen (¡No lo tenemos en ItemCarrito!)
    // Por ahora, usaremos un placeholder.
    // TODO: Obtener 'imagenResIdName' del Producto o añadirlo a 'ItemCarrito'
    val imageResId = remember {
        context.resources.getIdentifier("placeholder_image", "drawable", context.packageName)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = if (imageResId != 0) imageResId else android.R.drawable.ic_menu_gallery),
                contentDescription = item.nombre,
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 16.dp),
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
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

/**
 * Dibuja la fila del total y el botón de pagar.
 */
@Composable
fun TotalRow(total: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = MaterialTheme.shapes.extraSmall // Esquinas cuadradas
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
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
                onClick = { /* TODO: Ir a la pantalla de Checkout/Pagar */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = total > 0 // Deshabilitar si el total es 0
            ) {
                Text("Ir a Pagar", fontSize = 18.sp)
            }
        }
    }
}

// Función de formato (la copiamos de ProductDetailScreen)
fun Int.formatPrice(): String {
    return this.toString().reversed().chunked(3).joinToString(".").reversed()
}

// Preview para ver cómo queda
@Preview(showBackground = true)
@Composable
fun CartScreenPreview() {
    val fakeItems = listOf(
        ItemCarrito(1, "P1", "Torta de Chocolate", 45000, 1),
        ItemCarrito(2, "P2", "Mousse de Chocolate", 5000, 3)
    )
    PasteleriaMilSabores_Grupo9Theme {
        CartContent(items = fakeItems, total = 60000, onDeleteItem = {})
    }
}

package com.example.pasteleriamilsabores_grupo9.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
// import androidx.navigation.NavHostController // Not strictly needed here
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pasteleriamilsabores_grupo9.ui.products.ProductListScreen
import com.example.pasteleriamilsabores_grupo9.ui.cart.CartScreen
import com.example.pasteleriamilsabores_grupo9.ui.profile.ProfileScreen

// Definición de las pestañas
sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Inicio : BottomNavItem("inicio", Icons.Default.Home, "Inicio")
    object Carrito : BottomNavItem("carrito", Icons.Default.ShoppingCart, "Carrito")
    object Cuenta : BottomNavItem("cuenta", Icons.Default.AccountCircle, "Mi Cuenta")
    object Mas : BottomNavItem("mas", Icons.Default.MoreVert, "Más")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(mainNavController: NavController) { // Este es el NavController principal

    // Este es el NavController SOLO para las pestañas inferiores
    val bottomBarNavController = rememberNavController()

    val items = listOf(
        BottomNavItem.Inicio,
        BottomNavItem.Carrito,
        BottomNavItem.Cuenta,
        BottomNavItem.Mas
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Pastelería Mil Sabores",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: Abrir el cajón de filtros */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Filtros")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Ir a pantalla de notificaciones */ }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notificaciones")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by bottomBarNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            bottomBarNavController.navigate(item.route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        // NavHost para el contenido de las pestañas
        NavHost(
            navController = bottomBarNavController,
            startDestination = BottomNavItem.Inicio.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Inicio.route) {
                // Pasamos el NavController principal a la lista de productos
                ProductListScreen(navController = mainNavController)
            }
            composable(BottomNavItem.Carrito.route) {
                CartScreen() // No necesita el NavController principal (por ahora)
            }
            composable(BottomNavItem.Cuenta.route) {
                // --- 👇 AQUÍ ESTÁ EL CAMBIO 👇 ---
                // Le pasamos el NavController principal a ProfileScreen
                // para que pueda navegar a Login/Register
                ProfileScreen(navController = mainNavController)
                // --- 👆 FIN DEL CAMBIO 👆 ---
            }
            composable(BottomNavItem.Mas.route) {
                MoreScreen() // El placeholder para "Más"
            }
        }
    }
}

// Placeholder para la pestaña "Más"
@Composable
fun MoreScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Pantalla 'Más' (Ayuda, Políticas, etc.)")
    }
}
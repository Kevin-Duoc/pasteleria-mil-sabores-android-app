package com.example.pasteleriamilsabores_grupo9.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pasteleriamilsabores_grupo9.PasteleriaApplication
import com.example.pasteleriamilsabores_grupo9.ui.cart.CartScreen
import com.example.pasteleriamilsabores_grupo9.ui.more.MoreScreen
import com.example.pasteleriamilsabores_grupo9.ui.products.ProductListScreen
import com.example.pasteleriamilsabores_grupo9.ui.profile.ProfileScreen
import com.example.pasteleriamilsabores_grupo9.ui.theme.CremaOscuro
import com.example.pasteleriamilsabores_grupo9.viewmodel.ProductListViewModel
import com.example.pasteleriamilsabores_grupo9.viewmodel.ProductListViewModelFactory
import kotlinx.coroutines.launch

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Inicio : BottomNavItem("inicio", Icons.Default.Home, "Inicio")
    object Carrito : BottomNavItem("carrito", Icons.Default.ShoppingCart, "Carrito")
    object Cuenta : BottomNavItem("cuenta", Icons.Default.AccountCircle, "Mi Cuenta")
    object Mas : BottomNavItem("mas", Icons.Default.MoreVert, "Más")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(mainNavController: NavController) {

    val bottomBarNavController = rememberNavController()
    val items = listOf(
        BottomNavItem.Inicio,
        BottomNavItem.Carrito,
        BottomNavItem.Cuenta,
        BottomNavItem.Mas
    )
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val application = context.applicationContext as PasteleriaApplication
    val productListViewModel: ProductListViewModel = viewModel(
        factory = ProductListViewModelFactory(application.catalogRepository)
    )

    val categories by productListViewModel.categories.collectAsState()
    val selectedCategory by productListViewModel.selectedCategory.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = CremaOscuro
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Categorías",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = { scope.launch { drawerState.close() } }) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar menú", tint = MaterialTheme.colorScheme.primary)
                    }
                }
                Divider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))

                LazyColumn(modifier = Modifier.padding(vertical = 8.dp)) {
                    item {
                        FilterCheckboxItem(
                            text = "Ver Todo",
                            checked = selectedCategory == null,
                            onCheckedChange = { if (it) productListViewModel.filterByCategory(null) }
                        )
                    }
                    items(categories, key = { it.idCategoria }) { category ->
                        if (category.nombreCategoria != null) { // Solo muestra si el nombre no es nulo
                            FilterCheckboxItem(
                                text = category.nombreCategoria,
                                checked = selectedCategory == category.idCategoria,
                                onCheckedChange = { if (it) productListViewModel.filterByCategory(category.idCategoria) }
                            )
                        }
                    }
                }
            }
        },
        gesturesEnabled = true
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Pastelería Mil Sabores",
                            style = MaterialTheme.typography.headlineLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Filtros")
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* TODO: Notificaciones */ }) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notificaciones")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = CremaOscuro,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                        navigationIconContentColor = MaterialTheme.colorScheme.primary,
                        actionIconContentColor = MaterialTheme.colorScheme.primary
                    )
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
            NavHost(
                navController = bottomBarNavController,
                startDestination = BottomNavItem.Inicio.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(BottomNavItem.Inicio.route) {
                    ProductListScreen(
                        navController = mainNavController,
                        productListViewModel = productListViewModel
                    )
                }
                composable(BottomNavItem.Carrito.route) {
                    CartScreen(navController = mainNavController)
                }
                composable(BottomNavItem.Cuenta.route) {
                    ProfileScreen(navController = mainNavController)
                }
                composable(BottomNavItem.Mas.route) {
                    MoreScreen()
                }
            }
        }
    }
}

@Composable
fun FilterCheckboxItem(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(true) }
            .height(48.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = text,
            style = MaterialTheme. typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

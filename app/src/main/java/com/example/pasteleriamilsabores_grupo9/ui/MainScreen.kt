package com.example.pasteleriamilsabores_grupo9.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.pasteleriamilsabores_grupo9.viewmodel.Categorias
import com.example.pasteleriamilsabores_grupo9.viewmodel.ProductListViewModel
import com.example.pasteleriamilsabores_grupo9.viewmodel.ProductListViewModelFactory
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

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
    val productListViewModel: ProductListViewModel = viewModel(
        factory = ProductListViewModelFactory(
            (context.applicationContext as PasteleriaApplication).productoRepository
        )
    )

    val selectedCategories by productListViewModel.selectedCategories.collectAsState()
    val maxPrice by productListViewModel.maxPrice.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = CremaOscuro
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
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

                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    FilterCheckboxItem(
                        text = Categorias.VER_TODO,
                        checked = selectedCategories.contains(Categorias.VER_TODO),
                        onCheckedChange = {
                            productListViewModel.updateCategory(Categorias.VER_TODO, it)
                            scope.launch { drawerState.close() }
                        }
                    )
                    FilterCheckboxItem(
                        text = Categorias.TORTAS,
                        checked = selectedCategories.contains(Categorias.TORTAS),
                        onCheckedChange = { productListViewModel.updateCategory(Categorias.TORTAS, it) }
                    )
                    FilterCheckboxItem(
                        text = Categorias.POSTRES,
                        checked = selectedCategories.contains(Categorias.POSTRES),
                        onCheckedChange = { productListViewModel.updateCategory(Categorias.POSTRES, it) }
                    )
                    FilterCheckboxItem(
                        text = Categorias.SIN_AZUCAR,
                        checked = selectedCategories.contains(Categorias.SIN_AZUCAR),
                        onCheckedChange = { productListViewModel.updateCategory(Categorias.SIN_AZUCAR, it) }
                    )
                    FilterCheckboxItem(
                        text = Categorias.SIN_GLUTEN,
                        checked = selectedCategories.contains(Categorias.SIN_GLUTEN),
                        onCheckedChange = { productListViewModel.updateCategory(Categorias.SIN_GLUTEN, it) }
                    )
                    FilterCheckboxItem(
                        text = Categorias.VEGANO,
                        checked = selectedCategories.contains(Categorias.VEGANO),
                        onCheckedChange = { productListViewModel.updateCategory(Categorias.VEGANO, it) }
                    )
                }

                Divider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Rango de Precio:",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(8.dp))

                    val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
                    format.maximumFractionDigits = 0

                    Slider(
                        value = maxPrice,
                        onValueChange = { newValue ->
                            productListViewModel.updatePrice(newValue)
                        },
                        valueRange = 0f..60000f,
                        steps = 5,
                        modifier = Modifier.fillMaxWidth(),
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.primary,
                            inactiveTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                        )
                    )
                    Text(
                        text = "Hasta: ${format.format(maxPrice.toInt())}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.End)
                    )
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
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
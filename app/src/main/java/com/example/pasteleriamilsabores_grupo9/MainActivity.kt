package com.example.pasteleriamilsabores_grupo9

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pasteleriamilsabores_grupo9.ui.MainScreen
import com.example.pasteleriamilsabores_grupo9.ui.detail.ProductDetailScreen
import com.example.pasteleriamilsabores_grupo9.ui.login.LoginScreen
import com.example.pasteleriamilsabores_grupo9.ui.map.MapScreen
import com.example.pasteleriamilsabores_grupo9.ui.pedidos.MisPedidosScreen
import com.example.pasteleriamilsabores_grupo9.ui.register.RegisterScreen
import com.example.pasteleriamilsabores_grupo9.ui.theme.PasteleriaMilSabores_Grupo9Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PasteleriaMilSabores_Grupo9Theme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Routes.MAIN) {

                    composable(Routes.MAIN) { MainScreen(navController) }
                    composable(Routes.MAP) { MapScreen() } // <-- RUTA DEL MAPA AÑADIDA

                    composable(Routes.LOGIN) { LoginScreen(navController) }
                    composable(Routes.REGISTER) { RegisterScreen(navController) }

                    // Nueva pantalla de "Mis Pedidos"
                    composable(Routes.MIS_PEDIDOS) { MisPedidosScreen(navController) }

                    // Animación para el detalle de producto
                    composable(
                        route = Routes.PRODUCT_DETAIL,
                        arguments = listOf(navArgument("productId") { type = NavType.LongType }),
                        enterTransition = {
                            slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(300))
                        },
                        exitTransition = {
                            slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(300))
                        },
                        popEnterTransition = {
                            slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(300))
                        },
                        popExitTransition = {
                            slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(300))
                        }
                    ) { backStackEntry ->
                        val productId = backStackEntry.arguments?.getLong("productId")
                        ProductDetailScreen(navController = navController, productId = productId)
                    }
                }
            }
        }
    }
}

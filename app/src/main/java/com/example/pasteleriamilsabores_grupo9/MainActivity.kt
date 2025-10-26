package com.example.pasteleriamilsabores_grupo9

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pasteleriamilsabores_grupo9.ui.detail.ProductDetailScreen
import com.example.pasteleriamilsabores_grupo9.ui.login.LoginScreen
import com.example.pasteleriamilsabores_grupo9.ui.products.ProductListScreen
import com.example.pasteleriamilsabores_grupo9.ui.register.RegisterScreen
import com.example.pasteleriamilsabores_grupo9.ui.theme.PasteleriaMilSabores_Grupo9Theme
import com.example.pasteleriamilsabores_grupo9.ui.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PasteleriaMilSabores_Grupo9Theme {
                //El controlador de navegación
                val navController = rememberNavController()

                // NavHost es el contenedor que mostrará la pantalla actual
                NavHost(navController = navController, startDestination = Routes.MAIN) {

                    composable(Routes.MAIN) { MainScreen(navController) }
                    composable(Routes.PRODUCTS) { ProductListScreen(navController) }
                    composable(Routes.LOGIN) { LoginScreen(/*navController*/) }
                    composable(Routes.REGISTER) { RegisterScreen(/*navController*/) }
                    composable(
                        route = Routes.PRODUCT_DETAIL,
                        arguments = listOf(navArgument("productId") {type = NavType.StringType })
                    ) { backStackEntry ->
                        val productId = backStackEntry.arguments?.getString("productId")
                        ProductDetailScreen(navController = navController, productId = productId)
                    }
                }
            }
        }
    }
}
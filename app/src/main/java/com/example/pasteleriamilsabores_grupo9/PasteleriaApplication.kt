package com.example.pasteleriamilsabores_grupo9

import android.app.Application
import com.example.pasteleriamilsabores_grupo9.data.db.AppDatabase
import com.example.pasteleriamilsabores_grupo9.data.remote.api.AuthApiService
import com.example.pasteleriamilsabores_grupo9.data.remote.api.CatalogApiService
import com.example.pasteleriamilsabores_grupo9.data.remote.api.PedidoApiService
import com.example.pasteleriamilsabores_grupo9.repository.AuthRepository
import com.example.pasteleriamilsabores_grupo9.repository.CarritoRepository
import com.example.pasteleriamilsabores_grupo9.repository.CatalogRepository
import com.example.pasteleriamilsabores_grupo9.repository.PedidoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PasteleriaApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { AppDatabase.getDatabase(this) }

    // --- CLIENTES HTTP ---

    // Cliente OkHttp base (público para los servicios no autenticados)
    private val okHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    // Cliente OkHttp SEGURO para los servicios que requieren autenticación
    private val secureOkHttpClient by lazy {
        okHttpClient.newBuilder()
            .addInterceptor(Interceptor { chain ->
                // Obtenemos el token desde el repositorio.
                val token = authRepository.getAuthToken()
                val request = if (token != null) {
                    chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                } else {
                    chain.request()
                }
                chain.proceed(request)
            })
            .build()
    }

    // --- INSTANCIAS DE RETROFIT ---

    // Auth y Catalog usan el cliente http normal
    private val retrofitAuth by lazy {
        createRetrofitInstance("http://44.211.104.111:8081/", okHttpClient)
    }

    private val retrofitCatalog by lazy {
        createRetrofitInstance("http://52.73.124.122:8082/", okHttpClient)
    }

    // Pedidos usa el cliente http SEGURO
    private val retrofitPedidos by lazy {
        createRetrofitInstance("http://100.29.3.120:8083/", secureOkHttpClient)
    }

    // --- SERVICIOS DE API ---
    private val authApiService by lazy { retrofitAuth.create(AuthApiService::class.java) }
    private val catalogApiService by lazy { retrofitCatalog.create(CatalogApiService::class.java) }
    private val pedidoApiService by lazy { retrofitPedidos.create(PedidoApiService::class.java) }


    // --- REPOSITORIOS ---
    val authRepository by lazy { AuthRepository(authApiService, this, applicationScope) }
    val catalogRepository by lazy { CatalogRepository(catalogApiService) }
    val carritoRepository by lazy { CarritoRepository(database.carritoDao()) }
    // El PedidoRepository también necesitará el AuthRepository para obtener el ID de usuario.
    val pedidoRepository by lazy { PedidoRepository(pedidoApiService, authRepository) }


    // Función de ayuda para crear instancias de Retrofit
    private fun createRetrofitInstance(baseUrl: String, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

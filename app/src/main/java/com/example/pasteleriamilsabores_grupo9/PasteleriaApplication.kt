package com.example.pasteleriamilsabores_grupo9

import android.app.Application
import com.example.pasteleriamilsabores_grupo9.data.db.AppDatabase
import com.example.pasteleriamilsabores_grupo9.data.remote.AuthApiService
import com.example.pasteleriamilsabores_grupo9.data.remote.CatalogApiService
import com.example.pasteleriamilsabores_grupo9.repository.AuthRepository
import com.example.pasteleriamilsabores_grupo9.repository.CarritoRepository
import com.example.pasteleriamilsabores_grupo9.repository.CatalogRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PasteleriaApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { AppDatabase.getDatabase(this) }

    // Cliente OkHttp común para ambos servicios
    private val okHttpClient by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    // Instancia de Retrofit para el servicio de Autenticación
    private val retrofitAuth by lazy {
        Retrofit.Builder()
            .baseUrl("http://44.211.104.111:8081/") // URL del microservicio de Auth
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Instancia de Retrofit para el servicio de Catálogo
    private val retrofitCatalog by lazy {
        Retrofit.Builder()
            .baseUrl("http://52.73.124.122:8082/") // URL del microservicio de Catálogo
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val authApiService by lazy {
        retrofitAuth.create(AuthApiService::class.java)
    }

    private val catalogApiService by lazy {
        retrofitCatalog.create(CatalogApiService::class.java)
    }

    // Repositorios
    val carritoRepository by lazy { CarritoRepository(database.carritoDao()) }
    val authRepository by lazy { AuthRepository(authApiService, this, applicationScope) }
    val catalogRepository by lazy { CatalogRepository(catalogApiService) }
}

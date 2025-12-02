package com.example.pasteleriamilsabores_grupo9

import android.app.Application
import com.example.pasteleriamilsabores_grupo9.data.db.AppDatabase
import com.example.pasteleriamilsabores_grupo9.data.remote.AuthApiService
import com.example.pasteleriamilsabores_grupo9.repository.AuthRepository
import com.example.pasteleriamilsabores_grupo9.repository.CarritoRepository
import com.example.pasteleriamilsabores_grupo9.repository.ProductoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import com.example.pasteleriamilsabores_grupo9.data.sampleProductos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PasteleriaApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { AppDatabase.getDatabase(this) }

    // Configuración de Retrofit
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://44.211.104.111:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val authApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    // Repositorios
    val productoRepository by lazy { ProductoRepository(database.productoDao()) }
    val carritoRepository by lazy { CarritoRepository(database.carritoDao()) }
    val authRepository by lazy { AuthRepository(authApiService, this) } // Pasamos el contexto

    init {
        poblarBaseDeDatos()
    }

    private fun poblarBaseDeDatos() {
        applicationScope.launch(Dispatchers.IO) {
            val productoDao = database.productoDao()
            if (productoDao.count() == 0) {
                productoDao.insertAll(sampleProductos)
            }
        }
    }
}

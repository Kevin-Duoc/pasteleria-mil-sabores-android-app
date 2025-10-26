package com.example.pasteleriamilsabores_grupo9.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pasteleriamilsabores_grupo9.data.dao.CarritoDao
import com.example.pasteleriamilsabores_grupo9.data.dao.ProductoDao
import com.example.pasteleriamilsabores_grupo9.data.dao.UsuarioDao // <-- 1. AÑADIR IMPORT
import com.example.pasteleriamilsabores_grupo9.data.model.ItemCarrito
import com.example.pasteleriamilsabores_grupo9.data.model.Producto
import com.example.pasteleriamilsabores_grupo9.data.model.Usuario // <-- 2. AÑADIR IMPORT

@Database(
    entities = [
        Producto::class,
        ItemCarrito::class,
        Usuario::class // <-- 3. AÑADIR 'Usuario::class'
    ],
    version = 6,  // <-- 4. SUBIMOS A VERSIÓN 6
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productoDao(): ProductoDao
    abstract fun carritoDao(): CarritoDao
    abstract fun usuarioDao(): UsuarioDao // <-- 5. AÑADIR EL NUEVO DAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pasteleriamilsabores_database"
                )
                    // Ya no usamos el Callback, que era el que causaba el bug
                    .fallbackToDestructiveMigration() // Mantenemos esto
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
package com.example.pasteleriamilsabores_grupo9.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pasteleriamilsabores_grupo9.data.dao.CarritoDao
import com.example.pasteleriamilsabores_grupo9.data.dao.ProductoDao
import com.example.pasteleriamilsabores_grupo9.data.dao.UsuarioDao
import com.example.pasteleriamilsabores_grupo9.data.model.ItemCarrito
import com.example.pasteleriamilsabores_grupo9.data.model.Producto
import com.example.pasteleriamilsabores_grupo9.data.model.Usuario

@Database(
    entities = [
        Producto::class,
        ItemCarrito::class,
        Usuario::class
    ],
    version = 8,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productoDao(): ProductoDao
    abstract fun carritoDao(): CarritoDao
    abstract fun usuarioDao(): UsuarioDao

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
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
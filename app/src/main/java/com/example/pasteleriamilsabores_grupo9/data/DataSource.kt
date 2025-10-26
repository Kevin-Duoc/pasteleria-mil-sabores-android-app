package com.example.pasteleriamilsabores_grupo9.data

import com.example.pasteleriamilsabores_grupo9.data.model.Producto

// Lista completa de productos iniciales
val sampleProductos = listOf(
    Producto(id = "TC001", nombre = "Torta Cuadrada de Chocolate", descripcion = "Deliciosa torta de chocolate con capas de ganache y un toque de avellanas.", precio = 45000, imagenResIdName = "torta_cuadrada_chocolate", stock = 15, stockCritico = 5),
    Producto(id = "TC002", nombre = "Torta Cuadrada de Frutas", descripcion = "Una mezcla de frutas frescas y crema chantilly sobre un suave bizcocho de vainilla.", precio = 50000, imagenResIdName = "torta_cuadrada_frutas", stock = 8, stockCritico = 5),
    Producto(id = "TT001", nombre = "Torta Circular de Vainilla", descripcion = "Bizcocho de vainilla clásico relleno con crema pastelera.", precio = 40000, imagenResIdName = "torta_circular_vainilla", stock = 20, stockCritico = 10),
    Producto(id = "TT002", nombre = "Torta Circular de Manjar", descripcion = "Torta tradicional chilena con manjar y nueces.", precio = 42000, imagenResIdName = "torta_circular_manjar", stock = 10, stockCritico = 5), // Aumentado stock
    Producto(id = "PI001", nombre = "Mousse de Chocolate", descripcion = "Postre individual cremoso y suave, ideal para los amantes del chocolate.", precio = 5000, imagenResIdName = "mousse_chocolate", stock = 50, stockCritico = 20),
    Producto(id = "PI002", nombre = "Tiramisú Clásico", descripcion = "Un postre italiano individual con capas de café, mascarpone y cacao.", precio = 5500, imagenResIdName = "tiramisu_clasico", stock = 10, stockCritico = 5),
    Producto(id = "PSA001", nombre = "Torta Sin Azúcar de Naranja", descripcion = "Torta ligera y deliciosa, endulzada naturalmente.", precio = 48000, imagenResIdName = "torta_sin_azucar_naranja", stock = 12, stockCritico = 5),
    Producto(id = "PSA002", nombre = "Cheesecake Sin Azúcar", descripcion = "Suave y cremoso, este cheesecake es una opción perfecta para disfrutar sin culpa.", precio = 47000, imagenResIdName = "cheesecake_sin_azucar", stock = 8, stockCritico = 3), // Aumentado stock
    Producto(id = "PT001", nombre = "Empanada de Manzana", descripcion = "Pastelería tradicional rellena de manzanas especiadas.", precio = 3000, imagenResIdName = "empanada_manzana", stock = 100, stockCritico = 50),
    Producto(id = "PT002", nombre = "Tarta de Santiago", descripcion = "Tradicional tarta española hecha con almendras.", precio = 6000, imagenResIdName = "tarta_santiago", stock = 25, stockCritico = 10),
    Producto(id = "PG001", nombre = "Brownie Sin Gluten", descripcion = "Rico y denso, este brownie es perfecto para quienes necesitan evitar el gluten.", precio = 4000, imagenResIdName = "brownie_sin_gluten", stock = 8, stockCritico = 5),
    Producto(id = "PV001", nombre = "Torta Vegana de Chocolate", descripcion = "Torta de chocolate húmeda y deliciosa, hecha sin productos de origen animal.", precio = 50000, imagenResIdName = "torta_vegana_chocolate", stock = 7, stockCritico = 5),
    Producto(id = "TE001", nombre = "Torta Especial de Cumpleaños", descripcion = "Diseñada especialmente para celebraciones, personalizable con mensajes únicos.", precio = 55000, imagenResIdName = "torta_especial_cumpleanos", stock = 6, stockCritico = 5),
    Producto(id = "TE002", nombre = "Torta Especial de Boda", descripcion = "Elegante y deliciosa, centro de atención en cualquier boda.", precio = 60000, imagenResIdName = "torta_especial_boda", stock = 2, stockCritico = 1) // Puesto stock en 2 para probar el "Agotado"
)
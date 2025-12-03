package com.example.pasteleriamilsabores_grupo9.repository

import android.util.Log
import com.example.pasteleriamilsabores_grupo9.data.db.entity.CarritoItem
import com.example.pasteleriamilsabores_grupo9.data.remote.api.PedidoApiService
import com.example.pasteleriamilsabores_grupo9.data.remote.dto.pedidos.DetallePedidoDto
import com.example.pasteleriamilsabores_grupo9.data.remote.dto.pedidos.PedidoDto
import com.example.pasteleriamilsabores_grupo9.data.remote.dto.pedidos.PedidoResponseDto
import kotlinx.coroutines.flow.first

/**
 * Repositorio para gestionar las operaciones relacionadas con los pedidos.
 */
class PedidoRepository(
    private val pedidoApiService: PedidoApiService,
    private val authRepository: AuthRepository // 1. Añadimos el AuthRepository
) {

    /**
     * Crea un nuevo pedido en el servidor.
     */
    suspend fun crearPedido(items: List<CarritoItem>, userId: Long): Boolean {
        return try {
            val total = items.sumOf { it.precio * it.cantidad }
            val detalles = items.map {
                DetallePedidoDto(
                    idProductoRef = it.productId,
                    nombreProducto = it.nombre,
                    precioUnitario = it.precio,
                    cantidad = it.cantidad
                )
            }
            val pedidoDto = PedidoDto(
                idUsuarioRef = userId,
                total = total,
                detalles = detalles
            )
            val response = pedidoApiService.crearPedido(pedidoDto)
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("PedidoRepository", "Error al crear pedido", e)
            false
        }
    }

    /**
     * 2. Nueva función para obtener el historial de pedidos.
     */
    suspend fun getPedidos(): List<PedidoResponseDto> {
        return try {
            // Obtenemos el usuario actual que ha iniciado sesión
            val currentUser = authRepository.currentUser.first()

            if (currentUser != null) {
                // Si hay usuario, pedimos sus pedidos a la API
                pedidoApiService.getPedidosByUsuario(currentUser.id.toLong())
            } else {
                // Si no hay usuario, devolvemos una lista vacía
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("PedidoRepository", "Error al obtener los pedidos", e)
            emptyList() // En caso de cualquier error, devolvemos una lista vacía
        }
    }
}

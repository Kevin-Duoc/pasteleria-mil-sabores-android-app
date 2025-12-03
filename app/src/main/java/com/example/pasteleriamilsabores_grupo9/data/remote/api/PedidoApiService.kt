package com.example.pasteleriamilsabores_grupo9.data.remote.api

import com.example.pasteleriamilsabores_grupo9.data.remote.dto.pedidos.PedidoDto
import com.example.pasteleriamilsabores_grupo9.data.remote.dto.pedidos.PedidoResponseDto
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Interfaz de Retrofit para el microservicio de Pedidos.
 */
interface PedidoApiService {

    /**
     * Envía un nuevo pedido al servidor.
     */
    @POST("api/v1/pedidos")
    suspend fun crearPedido(@Body pedidoDto: PedidoDto): Response<ResponseBody>

    /**
     * Obtiene el historial de pedidos para un usuario específico.
     * @param usuarioId El ID del usuario.
     * @return Una lista con los pedidos del usuario.
     */
    @GET("api/v1/pedidos/usuario/{id}")
    suspend fun getPedidosByUsuario(@Path("id") usuarioId: Long): List<PedidoResponseDto>

}

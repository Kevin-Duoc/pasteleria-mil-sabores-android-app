package com.example.pasteleriamilsabores_grupo9.data.remote.api

import com.example.pasteleriamilsabores_grupo9.data.remote.dto.LoginRequest
import com.example.pasteleriamilsabores_grupo9.data.remote.dto.LoginResponse
import com.example.pasteleriamilsabores_grupo9.data.remote.dto.RegisterRequest
import com.example.pasteleriamilsabores_grupo9.data.remote.dto.UpdateProfileRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthApiService {
    @POST("api/v1/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST("api/v1/auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<Unit>

    @PUT("api/v1/auth/actualizar")
    suspend fun updateUser(@Body updateProfileRequest: UpdateProfileRequest): Response<String>
}

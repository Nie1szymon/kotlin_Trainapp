package com.example.trainappmobilev2.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val token: String, val userId: Int)
data class RegisterRequest(val username: String, val email: String, val password1: String, val password2: String)
data class RegisterResponse(val success: Boolean)

interface ApiService {
    @POST("api/auth2/login/")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("api/auth/register/")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>
}
package com.example.trainappmobilev2.network

import com.example.trainappmobilev2.model.AddUserPlanRequest
import com.example.trainappmobilev2.model.LoginRequest
import com.example.trainappmobilev2.model.LoginResponse
import com.example.trainappmobilev2.model.Plan
import com.example.trainappmobilev2.model.RegisterRequest
import com.example.trainappmobilev2.model.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("api/auth2/login/")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("api/auth/register/")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @GET("plans/")
    fun getPlans(@Header("Authorization") token: String): Call<List<Plan>>

    @POST("user/plans/")
    fun addUserPlan(@Header("Authorization") token: String, @Body request: AddUserPlanRequest): Call<Plan>
    @GET("user/plans/")
    fun getUserPlans(@Header("Authorization") token: String): Call<List<Plan>>

}
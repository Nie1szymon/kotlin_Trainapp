package com.example.trainappmobilev2.network

import com.example.trainappmobilev2.model.AddUserPlanRequest
import com.example.trainappmobilev2.model.LoginRequest
import com.example.trainappmobilev2.model.LoginResponse
import com.example.trainappmobilev2.model.Plan
import com.example.trainappmobilev2.model.RegisterRequest
import com.example.trainappmobilev2.model.RegisterResponse
import com.example.trainappmobilev2.model.Training
import com.example.trainappmobilev2.model.TrainingsExercises
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

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

    @GET("plans/{plan_id}/trainings/")
    fun getPlanTrainings(@Header("Authorization") token: String, @Path("plan_id") planId: Int): Call<List<Training>>

    @GET("trainings/{training_id}/exercises/")
    fun getTrainingExercises(@Header("Authorization") token: String, @Path("training_id") trainingId: Int): Call<List<TrainingsExercises>>
}
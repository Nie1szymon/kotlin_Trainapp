package com.example.trainappmobilev2.network

import com.example.trainappmobilev2.model.AddUserPlanRequest
import com.example.trainappmobilev2.model.Exercise
import com.example.trainappmobilev2.model.LoginRequest
import com.example.trainappmobilev2.model.LoginResponse
import com.example.trainappmobilev2.model.Plan
import com.example.trainappmobilev2.model.RegisterRequest
import com.example.trainappmobilev2.model.RegisterResponse
import com.example.trainappmobilev2.model.Training
import com.example.trainappmobilev2.model.TrainingExerciseAdd
import com.example.trainappmobilev2.model.TrainingsExercises
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("api/auth2/login/")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("api/auth/register/")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @GET("plans/")
    fun getPlans(@Header("Authorization") token: String): Call<List<Plan>>

    @GET("user/owned-plans/")
    fun getPlansOwner(@Header("Authorization") token: String): Call<List<Plan>>

    @GET("plans/{plan_id}/")
    fun getPlanDetails(@Path("plan_id") planId: Int): Call<Plan>

    @POST("user/plans/")
    fun addUserPlan(@Header("Authorization") token: String, @Body request: AddUserPlanRequest): Call<Plan>

    @GET("user/plans/")
    fun getUserPlans(@Header("Authorization") token: String): Call<List<Plan>>

    @PUT("plans/{plan_id}/")
    fun updatePlan(@Path("plan_id") planId: Int, @Body plan: Plan): Call<Plan>

    @GET("plans/{plan_id}/trainings/")
    fun getPlanTrainings(@Header("Authorization") token: String, @Path("plan_id") planId: Int): Call<List<Training>>

    @POST("plans/")
    fun addPlan(@Header("Authorization") token: String, @Body plan: Plan): Call<Plan>

    @GET("trainings/{training_id}/exercises/")
    fun getTrainingExercises(@Header("Authorization") token: String, @Path("training_id") trainingId: Int): Call<List<TrainingsExercises>>

    @GET("trainings/{training_id}/")
    fun getTrainingDetails(@Path("training_id") trainingId: Int): Call<Training>

    @PUT("trainings/{training_id}/")
    fun updateTraining(@Path("training_id") trainingId: Int, @Body training: Training): Call<Training>

    @PUT("trainings/exercises/{id}/")
    fun updateTrainingExercise(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body trainingExercise: TrainingsExercises
    ): Call<TrainingsExercises>

    @DELETE("trainings/exercises/{id}/")
    fun deleteTrainingExercise(@Header("Authorization") token: String, @Path("id") id: TrainingsExercises): Call<Void>


    @POST("trainings/{training_id}/exercises/")
    fun addTrainingExercise(
        @Header("Authorization") token: String,
        @Path("training_id") trainingId: Int,
        @Body trainingExercise: TrainingExerciseAdd
    ): Call<TrainingExerciseAdd>


    @GET("exercises/")
    fun getExercises(@Header("Authorization") token: String): Call<List<Exercise>>

    @POST("plans/{plan_id}/trainings/")
    fun addTraining(@Header("Authorization") token: String, @Path("plan_id") planId: Int, @Body training: Training): Call<Training>
}
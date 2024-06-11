package com.example.trainappmobilev2.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PixabayRetrofitClient {
    private const val BASE_URL = "https://pixabay.com/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: PixabayApiService by lazy {
        retrofit.create(PixabayApiService::class.java)
    }
}
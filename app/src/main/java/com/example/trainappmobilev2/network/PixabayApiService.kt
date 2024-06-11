package com.example.trainappmobilev2.network

import com.example.trainappmobilev2.model.PixabayImageResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApiService {
    @GET("api/")
    fun searchPixabayImages(
        @Query("q") query: String,
        @Query("key") apiKey: String = "44328814-0ee5574a9884aa9711029cf64"
    ): Call<PixabayImageResponse>
}
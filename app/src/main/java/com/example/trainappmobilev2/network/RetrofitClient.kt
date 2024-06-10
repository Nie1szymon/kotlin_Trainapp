package com.example.trainappmobilev2.network


import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import com.google.gson.GsonBuilder
import java.io.IOException

object RetrofitClient {
    private const val BASE_URL = "http://192.168.0.130:8000/"
    private var csrfToken: String? = null
    private val cookieStore: HashMap<String, List<Cookie>> = HashMap()

    private val cookieJar = object : CookieJar {
        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            cookieStore[url.host] = cookies
            for (cookie in cookies) {
                if (cookie.name.equals("csrftoken", ignoreCase = true)) {
                    csrfToken = cookie.value
                }
            }
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            return cookieStore[url.host] ?: listOf()
        }
    }

    // Add logging
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .cookieJar(cookieJar)
        .addInterceptor(loggingInterceptor) // Add logging
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val newRequest = originalRequest.newBuilder().apply {
                csrfToken?.let {
                    addHeader("X-CSRFToken", it)
                }
            }.build()
            chain.proceed(newRequest)
        }.build()

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create()) // Add text converter
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

        retrofit.create(ApiService::class.java)
    }

    @Throws(IOException::class)
    fun fetchCsrfToken() {
        val request = Request.Builder()
            .url("${BASE_URL}api/auth/csrf/")
            .build()

        okHttpClient.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                val responseBody = response.body?.string() ?: ""
                println("CSRF Response: $responseBody")
                // Check if response is a string (error message)
                if (!responseBody.startsWith("{")) {
                    // Handle string response (e.g., display error message)
                    println("Login Failed: $responseBody")
                } else {
                    // Parse JSON object if it's valid (unlikely in this case)
                    // ...
                }
            } else {
                println("CSRF Token Fetch Failed: ${response.code}")
            }
        }
    }
}

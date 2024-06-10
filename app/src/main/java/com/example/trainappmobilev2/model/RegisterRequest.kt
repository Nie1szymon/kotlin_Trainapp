package com.example.trainappmobilev2.model

data class RegisterRequest(
    val username: String,
    val email: String,
    val password1: String,
    val password2: String
)

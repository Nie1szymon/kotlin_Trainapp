package com.example.trainappmobilev2.model


data class Plan(
    val id: Int = 0,  // Domyślna wartość dla 'id'
    val name: String,
    val desc: String,
    val price: String = "0.00",  // Dodanie domyślnej wartości dla 'price'
    val owner: String = "unknown"  // Domyślna wartość dla 'owner'
)
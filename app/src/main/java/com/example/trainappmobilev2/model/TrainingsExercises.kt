package com.example.trainappmobilev2.model

data class TrainingsExercises(
    val trainings: Int,
    val exercises_name: String,
    val exercises_desc: String,
    var series: Int,
    var repeat: Int
)
package com.example.trainappmobilev2

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trainappmobilev2.model.Exercise
import com.example.trainappmobilev2.model.TrainingExerciseAdd
import com.example.trainappmobilev2.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddTrainingExerciseActivity : AppCompatActivity() {

    private lateinit var spinnerExercises: Spinner
    private lateinit var editTextSeries: EditText
    private lateinit var editTextRepeat: EditText
    private lateinit var buttonAddExercise: Button
    private var trainingId: Int = -1
    private var exercisesList: List<Exercise> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_training_exercise)

        spinnerExercises = findViewById(R.id.spinnerExercises)
        editTextSeries = findViewById(R.id.editTextSeries)
        editTextRepeat = findViewById(R.id.editTextRepeat)
        buttonAddExercise = findViewById(R.id.buttonAddExercise)

        trainingId = intent.getIntExtra("TRAINING_ID", -1)

        buttonAddExercise.setOnClickListener {
            addExerciseToTraining()
        }

        fetchExercises()
    }

    private fun fetchExercises() {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("access_token", null)

        if (token != null) {
            RetrofitClient.instance.getExercises("Bearer $token").enqueue(object : Callback<List<Exercise>> {
                override fun onResponse(call: Call<List<Exercise>>, response: Response<List<Exercise>>) {
                    if (response.isSuccessful) {
                        exercisesList = response.body() ?: listOf()
                        Log.d("AddTrainingExercise", "Fetched exercises: $exercisesList")
                        setupSpinner()
                    } else {
                        Log.e("AddTrainingExercise", "Failed to fetch exercises: ${response.errorBody()?.string()}")
                        Toast.makeText(this@AddTrainingExerciseActivity, "Failed to fetch exercises", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Exercise>>, t: Throwable) {
                    Log.e("AddTrainingExercise", "Network error: ${t.message}")
                    Toast.makeText(this@AddTrainingExerciseActivity, "Network error", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "No token found, please login", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupSpinner() {
        if (exercisesList.isEmpty()) {
            Toast.makeText(this, "No exercises available", Toast.LENGTH_SHORT).show()
        } else {
            val exerciseNames = exercisesList.map { it.name }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, exerciseNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerExercises.adapter = adapter
        }
    }

    private fun addExerciseToTraining() {
        val selectedExercise = exercisesList[spinnerExercises.selectedItemPosition]
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("access_token", null)

        if (token != null) {
            val series = editTextSeries.text.toString().toIntOrNull() ?: 0
            val repeat = editTextRepeat.text.toString().toIntOrNull() ?: 0

            val trainingExercise = TrainingExerciseAdd(
                trainings = trainingId,
                exercises = selectedExercise.id,
                series = series,
                repeat = repeat
            )

            RetrofitClient.instance.addTrainingExercise("Bearer $token", trainingId, trainingExercise).enqueue(object : Callback<TrainingExerciseAdd> {
                override fun onResponse(call: Call<TrainingExerciseAdd>, response: Response<TrainingExerciseAdd>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddTrainingExerciseActivity, "Exercise added to training", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Log.e("AddTrainingExercise", "Failed to add exercise: ${response.errorBody()?.string()}")
                        Toast.makeText(this@AddTrainingExerciseActivity, "Failed to add exercise", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<TrainingExerciseAdd>, t: Throwable) {
                    Log.e("AddTrainingExercise", "Network error: ${t.message}")
                    Toast.makeText(this@AddTrainingExerciseActivity, "Network error", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "No token found, please login", Toast.LENGTH_SHORT).show()
        }
    }
}




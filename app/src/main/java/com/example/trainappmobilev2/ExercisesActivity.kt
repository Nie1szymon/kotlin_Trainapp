package com.example.trainappmobilev2

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trainappmobilev2.R
import com.example.trainappmobilev2.model.TrainingsExercises
import com.example.trainappmobilev2.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExercisesActivity : AppCompatActivity() {

    private lateinit var exercisesRecyclerView: RecyclerView
    private lateinit var exercisesAdapter: ExercisesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercises)

        exercisesRecyclerView = findViewById(R.id.exercisesRecyclerView)
        exercisesRecyclerView.layoutManager = LinearLayoutManager(this)
        exercisesAdapter = ExercisesAdapter()
        exercisesRecyclerView.adapter = exercisesAdapter

        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("access_token", null)
        val trainingId = intent.getIntExtra("TRAINING_ID", -1)

        if (token != null && trainingId != -1) {
            Log.d("ExercisesActivity", "Token: $token, Training ID: $trainingId")
            fetchExercises(token, trainingId)
        } else {
            Log.d("ExercisesActivity", "No token found or invalid Training ID")
            Toast.makeText(this, "No token found or invalid Training ID", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchExercises(token: String, trainingId: Int) {
        val apiService = RetrofitClient.instance
        apiService.getTrainingExercises("Bearer $token", trainingId).enqueue(object : Callback<List<TrainingsExercises>> {
            override fun onResponse(call: Call<List<TrainingsExercises>>, response: Response<List<TrainingsExercises>>) {
                if (response.isSuccessful) {
                    val exercises = response.body()
                    exercises?.let {
                        Log.d("ExercisesActivity", "Exercises: $it")
                        exercisesAdapter.setExercises(it)
                    }
                } else {
                    Log.e("ExercisesActivity", "Error: ${response.errorBody()?.string()}")
                    Toast.makeText(this@ExercisesActivity, "Failed to load exercises", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<TrainingsExercises>>, t: Throwable) {
                Log.e("ExercisesActivity", "Network error", t)
                Toast.makeText(this@ExercisesActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
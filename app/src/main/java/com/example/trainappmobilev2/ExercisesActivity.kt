package com.example.trainappmobilev2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.trainappmobilev2.MainActivity
import com.example.trainappmobilev2.R
import com.example.trainappmobilev2.model.TrainingsExercises
import com.example.trainappmobilev2.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExercisesActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var exercisesAdapter: ExercisesPagerAdapter
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercises)

        viewPager = findViewById(R.id.viewPager)
        nextButton = findViewById(R.id.nextButton)

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

        nextButton.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem < exercisesAdapter.itemCount - 1) {
                viewPager.currentItem = currentItem + 1
            } else {
                // Last page, go to MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == exercisesAdapter.itemCount - 1) {
                    nextButton.text = "Finish"
                } else {
                    nextButton.text = "Next"
                }
            }
        })
    }

    private fun fetchExercises(token: String, trainingId: Int) {
        val apiService = RetrofitClient.instance
        apiService.getTrainingExercises("Bearer $token", trainingId).enqueue(object : Callback<List<TrainingsExercises>> {
            override fun onResponse(call: Call<List<TrainingsExercises>>, response: Response<List<TrainingsExercises>>) {
                if (response.isSuccessful) {
                    val exercises = response.body()
                    exercises?.let {
                        Log.d("ExercisesActivity", "Exercises: $it")
                        exercisesAdapter = ExercisesPagerAdapter(it)
                        viewPager.adapter = exercisesAdapter
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
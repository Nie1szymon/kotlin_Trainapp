package com.example.trainappmobilev2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trainappmobilev2.model.TrainingsExercises
import com.example.trainappmobilev2.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditExercisesActivity : AppCompatActivity() {

    private lateinit var exercisesRecyclerView: RecyclerView
    private lateinit var buttonAddExercise: Button
    private lateinit var exercisesAdapter: ExercisesEditAdapter
    private var trainingId: Int = -1
    private var exercisesList: MutableList<TrainingsExercises> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_exercises)

        exercisesRecyclerView = findViewById(R.id.exercisesRecyclerView)
        buttonAddExercise = findViewById(R.id.buttonAddExercise)

        trainingId = intent.getIntExtra("TRAINING_ID", -1)

        exercisesRecyclerView.layoutManager = LinearLayoutManager(this)
        exercisesAdapter = ExercisesEditAdapter(exercisesList, { exercise -> editExercise(exercise) }, { exercise -> deleteExercise(exercise) })
        exercisesRecyclerView.adapter = exercisesAdapter

        buttonAddExercise.setOnClickListener {
            val intent = Intent(this, AddTrainingExerciseActivity::class.java)
            intent.putExtra("TRAINING_ID", trainingId)
            startActivity(intent)
        }

        fetchExercises(trainingId)
    }

    private fun fetchExercises(trainingId: Int) {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("access_token", null)

        if (token != null) {
            RetrofitClient.instance.getTrainingExercises("Bearer $token", trainingId).enqueue(object : Callback<List<TrainingsExercises>> {
                override fun onResponse(call: Call<List<TrainingsExercises>>, response: Response<List<TrainingsExercises>>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            exercisesList.clear()
                            exercisesList.addAll(it)
                            exercisesAdapter.notifyDataSetChanged()
                        }
                    } else {
                        Toast.makeText(this@EditExercisesActivity, "Failed to fetch exercises", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<TrainingsExercises>>, t: Throwable) {
                    Toast.makeText(this@EditExercisesActivity, "Network error", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "No token found, please login", Toast.LENGTH_SHORT).show()
        }
    }

    private fun editExercise(trainingExercise: TrainingsExercises) {
        // Implement edit exercise logic here
    }

    private fun deleteExercise(trainingExercise: TrainingsExercises) {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("access_token", null)

        if (token != null) {
            RetrofitClient.instance.deleteTrainingExercise("Bearer $token", trainingExercise).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        exercisesList.remove(trainingExercise)
                        exercisesAdapter.notifyDataSetChanged()
                        Toast.makeText(this@EditExercisesActivity, "Exercise deleted", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@EditExercisesActivity, "Failed to delete exercise", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@EditExercisesActivity, "Network error", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "No token found, please login", Toast.LENGTH_SHORT).show()
        }
    }
}






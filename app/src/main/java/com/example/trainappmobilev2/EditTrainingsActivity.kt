package com.example.trainappmobilev2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trainappmobilev2.model.Training
import com.example.trainappmobilev2.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditTrainingsActivity : AppCompatActivity() {

    private lateinit var trainingsRecyclerView: RecyclerView
    private lateinit var trainingsAdapter: TrainingsAdapter
    private var planId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_trainings)

        trainingsRecyclerView = findViewById(R.id.trainingsRecyclerView)
        trainingsRecyclerView.layoutManager = LinearLayoutManager(this)

        planId = intent.getIntExtra("PLAN_ID", -1)
        if (planId != -1) {
            fetchTrainings(planId)
        }
    }

    private fun fetchTrainings(planId: Int) {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("access_token", null)

        if (token != null) {
            RetrofitClient.instance.getPlanTrainings("Bearer $token", planId).enqueue(object : Callback<List<Training>> {
                override fun onResponse(call: Call<List<Training>>, response: Response<List<Training>>) {
                    if (response.isSuccessful) {
                        response.body()?.let { trainings ->
                            trainingsAdapter = TrainingsAdapter(trainings, "Edit Training", { training, planId ->
                                val intent = Intent(this@EditTrainingsActivity, EditExercisesActivity::class.java)
                                intent.putExtra("TRAINING_ID", training.id)
                                intent.putExtra("PLAN_ID", planId)
                                startActivity(intent)
                            }, planId)
                            trainingsRecyclerView.adapter = trainingsAdapter
                        }
                    } else {
                        Toast.makeText(this@EditTrainingsActivity, "Failed to fetch trainings", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Training>>, t: Throwable) {
                    Toast.makeText(this@EditTrainingsActivity, "Network error", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "No token found, please login", Toast.LENGTH_SHORT).show()
        }
    }
}


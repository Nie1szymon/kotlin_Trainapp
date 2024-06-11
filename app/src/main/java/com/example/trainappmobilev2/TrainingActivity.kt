package com.example.trainappmobilev2

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trainappmobilev2.R
import com.example.trainappmobilev2.model.Training
import com.example.trainappmobilev2.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrainingActivity : AppCompatActivity() {

    private lateinit var trainingRecyclerView: RecyclerView
    private lateinit var trainingAdapter: TrainingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)

        trainingRecyclerView = findViewById(R.id.trainingRecyclerView)
        trainingRecyclerView.layoutManager = LinearLayoutManager(this)
        trainingAdapter = TrainingAdapter()
        trainingRecyclerView.adapter = trainingAdapter

        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("access_token", null)
        val planId = intent.getIntExtra("PLAN_ID", -1)

        if (token != null && planId != -1) {
            Log.d("TrainingActivity", "Token: $token, Plan ID: $planId")
            fetchTrainings(token, planId)
        } else {
            Log.d("TrainingActivity", "No token found or invalid Plan ID")
            Toast.makeText(this, "No token found or invalid Plan ID", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchTrainings(token: String, planId: Int) {
        val apiService = RetrofitClient.instance
        apiService.getPlanTrainings("Bearer $token", planId).enqueue(object : Callback<List<Training>> {
            override fun onResponse(call: Call<List<Training>>, response: Response<List<Training>>) {
                if (response.isSuccessful) {
                    val trainings = response.body()
                    trainings?.let {
                        Log.d("TrainingActivity", "Trainings: $it")
                        trainingAdapter.setTrainings(it)
                    }
                } else {
                    Log.e("TrainingActivity", "Error: ${response.errorBody()?.string()}")
                    Toast.makeText(this@TrainingActivity, "Failed to load trainings", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Training>>, t: Throwable) {
                Log.e("TrainingActivity", "Network error", t)
                Toast.makeText(this@TrainingActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }}

package com.example.trainappmobilev2

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trainappmobilev2.R
import com.example.trainappmobilev2.model.AddUserPlanRequest
import com.example.trainappmobilev2.model.Plan
import com.example.trainappmobilev2.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlansActivity : AppCompatActivity() {

    private lateinit var plansRecyclerView: RecyclerView
    private lateinit var plansAdapter: PlansAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plans)

        plansRecyclerView = findViewById(R.id.plansRecyclerView)
        plansRecyclerView.layoutManager = LinearLayoutManager(this)
        plansAdapter = PlansAdapter({ plan -> addUserPlan(plan) }, "Add to My Plans")
        plansRecyclerView.adapter = plansAdapter

        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("access_token", null)

        if (token != null) {
            Log.d("PlansActivity", "Token: $token")
            fetchPlans(token)
        } else {
            Log.d("PlansActivity", "No token found")
            Toast.makeText(this, "No token found, please login", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchPlans(token: String) {
        val apiService = RetrofitClient.instance
        apiService.getPlans("Bearer $token").enqueue(object : Callback<List<Plan>> {
            override fun onResponse(call: Call<List<Plan>>, response: Response<List<Plan>>) {
                if (response.isSuccessful) {
                    val plans = response.body()
                    plans?.let {
                        Log.d("PlansActivity", "Plans: $it")
                        plansAdapter.setPlans(it)
                    }
                } else {
                    Log.e("PlansActivity", "Error: ${response.errorBody()?.string()}")
                    Toast.makeText(this@PlansActivity, "Failed to load plans", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Plan>>, t: Throwable) {
                Log.e("PlansActivity", "Network error", t)
                Toast.makeText(this@PlansActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addUserPlan(plan: Plan) {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("access_token", null)

        if (token != null) {
            val apiService = RetrofitClient.instance
            val request = AddUserPlanRequest(plan.id)
            apiService.addUserPlan("Bearer $token", request).enqueue(object : Callback<Plan> {
                override fun onResponse(call: Call<Plan>, response: Response<Plan>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@PlansActivity, "Plan added to your plans", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("PlansActivity", "Error: ${response.errorBody()?.string()}")
                        Toast.makeText(this@PlansActivity, "Failed to add plan", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Plan>, t: Throwable) {
                    Log.e("PlansActivity", "Network error", t)
                    Toast.makeText(this@PlansActivity, "Network error", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "No token found, please login", Toast.LENGTH_SHORT).show()
        }
    }
}
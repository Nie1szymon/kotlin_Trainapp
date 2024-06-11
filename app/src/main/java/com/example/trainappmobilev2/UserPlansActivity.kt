package com.example.trainappmobilev2

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trainappmobilev2.R
import com.example.trainappmobilev2.model.Plan
import com.example.trainappmobilev2.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserPlansActivity : AppCompatActivity() {

    private lateinit var userPlansRecyclerView: RecyclerView
    private lateinit var userPlansAdapter: PlansAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_plans)

        userPlansRecyclerView = findViewById(R.id.userPlansRecyclerView)
        userPlansRecyclerView.layoutManager = LinearLayoutManager(this)
        userPlansAdapter = PlansAdapter { plan -> addUserPlan(plan) }
        userPlansRecyclerView.adapter = userPlansAdapter

        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("access_token", null)

        if (token != null) {
            Log.d("UserPlansActivity", "Token: $token")
            fetchUserPlans(token)
        } else {
            Log.d("UserPlansActivity", "No token found")
            Toast.makeText(this, "No token found, please login", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchUserPlans(token: String) {
        val apiService = RetrofitClient.instance
        apiService.getUserPlans("Bearer $token").enqueue(object : Callback<List<Plan>> {
            override fun onResponse(call: Call<List<Plan>>, response: Response<List<Plan>>) {
                if (response.isSuccessful) {
                    val plans = response.body()
                    plans?.let {
                        Log.d("UserPlansActivity", "Plans: $it")
                        userPlansAdapter.setPlans(it)
                    }
                } else {
                    Log.e("UserPlansActivity", "Error: ${response.errorBody()?.string()}")
                    Toast.makeText(this@UserPlansActivity, "Failed to load user plans", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Plan>>, t: Throwable) {
                Log.e("UserPlansActivity", "Network error", t)
                Toast.makeText(this@UserPlansActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addUserPlan(plan: Plan) {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("access_token", null)

        if (token != null) {
            val apiService = RetrofitClient.instance
            apiService.addUserPlan("Bearer $token", plan).enqueue(object : Callback<Plan> {
                override fun onResponse(call: Call<Plan>, response: Response<Plan>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@UserPlansActivity, "Plan added to your plans", Toast.LENGTH_SHORT).show()
                        // Optional: refresh the list
                        fetchUserPlans(token)
                    } else {
                        Log.e("UserPlansActivity", "Error: ${response.errorBody()?.string()}")
                        Toast.makeText(this@UserPlansActivity, "Failed to add plan", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Plan>, t: Throwable) {
                    Log.e("UserPlansActivity", "Network error", t)
                    Toast.makeText(this@UserPlansActivity, "Network error", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "No token found, please login", Toast.LENGTH_SHORT).show()
        }
    }
}
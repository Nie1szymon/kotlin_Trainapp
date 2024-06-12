package com.example.trainappmobilev2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trainappmobilev2.model.Plan
import com.example.trainappmobilev2.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditPlansActivity : AppCompatActivity() {

    private lateinit var plansRecyclerView: RecyclerView
    private lateinit var plansAdapter: PlansAdapter
    private lateinit var addPlanButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_plans)

        plansRecyclerView = findViewById(R.id.plansRecyclerView)
        addPlanButton = findViewById(R.id.addPlanButton)

        plansRecyclerView.layoutManager = LinearLayoutManager(this)
        plansAdapter = PlansAdapter({ plan ->
            val intent = Intent(this, EditPlanActivity::class.java)
            intent.putExtra("PLAN_ID", plan.id)
            startActivityForResult(intent, REQUEST_EDIT_PLAN)
        }, "Edit Plan")
        plansRecyclerView.adapter = plansAdapter

        addPlanButton.setOnClickListener {
            val intent = Intent(this, AddPlanActivity::class.java)
            startActivity(intent)
        }

        fetchPlans()
    }

    private fun fetchPlans() {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("access_token", null)

        if (token != null) {
            RetrofitClient.instance.getPlansOwner("Bearer $token").enqueue(object : Callback<List<Plan>> {
                override fun onResponse(call: Call<List<Plan>>, response: Response<List<Plan>>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            plansAdapter.setPlans(it)
                        }
                    } else {
                        Toast.makeText(this@EditPlansActivity, "Failed to fetch plans", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Plan>>, t: Throwable) {
                    Toast.makeText(this@EditPlansActivity, "Network error", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "No token found, please login", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EDIT_PLAN && resultCode == RESULT_OK) {
            fetchPlans() // Ponownie załaduj dane po powrocie z EditPlanActivity
        }
    }

    companion object {
        const val REQUEST_EDIT_PLAN = 1
    }
}

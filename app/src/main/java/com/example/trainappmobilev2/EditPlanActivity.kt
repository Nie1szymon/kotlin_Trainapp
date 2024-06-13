package com.example.trainappmobilev2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trainappmobilev2.model.Plan
import com.example.trainappmobilev2.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditPlanActivity : AppCompatActivity() {

    private lateinit var editTextPlanName: EditText
    private lateinit var editTextPlanDesc: EditText
    private lateinit var editTextPlanPrice: EditText
    private lateinit var buttonSavePlan: Button
    private lateinit var buttonEditTrainings: Button
    private lateinit var buttonAddTraining: Button
    private var planId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_plan)

        editTextPlanName = findViewById(R.id.editTextPlanName)
        editTextPlanDesc = findViewById(R.id.editTextPlanDesc)
        editTextPlanPrice = findViewById(R.id.editTextPlanPrice)
        buttonSavePlan = findViewById(R.id.buttonSavePlan)
        buttonEditTrainings = findViewById(R.id.buttonEditTrainings)
        buttonAddTraining = findViewById(R.id.buttonAddTraining)

        // Get plan ID from Intent
        planId = intent.getIntExtra("PLAN_ID", -1)
        if (planId != -1) {
            fetchPlanDetails(planId)
        }

        buttonSavePlan.setOnClickListener {
            savePlanDetails()
        }

        buttonEditTrainings.setOnClickListener {
            val intent = Intent(this, EditTrainingsActivity::class.java)
            intent.putExtra("PLAN_ID", planId)
            startActivity(intent)
        }

        buttonAddTraining.setOnClickListener {
            val intent = Intent(this, AddTrainingActivity::class.java)
            intent.putExtra("PLAN_ID", planId)
            startActivity(intent)
        }
    }

    private fun fetchPlanDetails(planId: Int) {
        val apiService = RetrofitClient.instance
        apiService.getPlanDetails(planId).enqueue(object : Callback<Plan> {
            override fun onResponse(call: Call<Plan>, response: Response<Plan>) {
                if (response.isSuccessful) {
                    response.body()?.let { plan ->
                        editTextPlanName.setText(plan.name)
                        editTextPlanDesc.setText(plan.desc)
                        editTextPlanPrice.setText(plan.price.toString())
                    }
                } else {
                    Toast.makeText(this@EditPlanActivity, "Failed to fetch plan details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Plan>, t: Throwable) {
                Toast.makeText(this@EditPlanActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun savePlanDetails() {
        val name = editTextPlanName.text.toString()
        val desc = editTextPlanDesc.text.toString()
        val price = editTextPlanPrice.text.toString().toDouble()

        val apiService = RetrofitClient.instance
        val plan = Plan(id = planId, name = name, desc = desc, price = price.toString(), owner = "")

        apiService.updatePlan(planId, plan).enqueue(object : Callback<Plan> {
            override fun onResponse(call: Call<Plan>, response: Response<Plan>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EditPlanActivity, "Plan updated successfully", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this@EditPlanActivity, "Failed to update plan", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Plan>, t: Throwable) {
                Toast.makeText(this@EditPlanActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}


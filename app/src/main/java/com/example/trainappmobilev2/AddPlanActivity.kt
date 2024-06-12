package com.example.trainappmobilev2

import android.annotation.SuppressLint
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

class AddPlanActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var descEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var addPlanButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_plan)

        nameEditText = findViewById(R.id.nameEditText)
        descEditText = findViewById(R.id.descEditText)
        priceEditText = findViewById(R.id.priceEditText)
        addPlanButton = findViewById(R.id.addPlanButton)

        addPlanButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val desc = descEditText.text.toString()
            val price = priceEditText.text.toString()

            if (name.isNotEmpty() && desc.isNotEmpty() && price.isNotEmpty()) {
                addPlan(Plan(name = name, desc = desc, price = price))
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addPlan(plan: Plan) {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("access_token", null)

        if (token != null) {
            RetrofitClient.instance.addPlan("Bearer $token", plan).enqueue(object : Callback<Plan> {
                override fun onResponse(call: Call<Plan>, response: Response<Plan>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddPlanActivity, "Plan added successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@AddPlanActivity, "Failed to add plan", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Plan>, t: Throwable) {
                    Toast.makeText(this@AddPlanActivity, "Network error", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "No token found, please login", Toast.LENGTH_SHORT).show()
        }
    }
}
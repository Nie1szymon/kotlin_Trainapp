package com.example.trainappmobilev2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trainappmobilev2.model.Training
import com.example.trainappmobilev2.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddTrainingActivity : AppCompatActivity() {

    private lateinit var editTextTrainingName: EditText
    private lateinit var editTextTrainingDesc: EditText
    private lateinit var editTextTrainingDate: EditText
    private lateinit var buttonSaveTraining: Button
    private var planId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_training)

        editTextTrainingName = findViewById(R.id.editTextTrainingName)
        editTextTrainingDesc = findViewById(R.id.editTextTrainingDesc)
        editTextTrainingDate = findViewById(R.id.editTextTrainingDate)
        buttonSaveTraining = findViewById(R.id.buttonSaveTraining)

        planId = intent.getIntExtra("PLAN_ID", -1)

        buttonSaveTraining.setOnClickListener {
            addTraining()
        }
    }

    private fun addTraining() {
        val name = editTextTrainingName.text.toString()
        val desc = editTextTrainingDesc.text.toString()
        val date = editTextTrainingDate.text.toString()

        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("access_token", null)

        if (token != null) {
            val training = Training(
                id = 0, // Assuming 0 for new training
                name = name,
                desc = desc,
                date = date
            )

            RetrofitClient.instance.addTraining("Bearer $token", planId, training).enqueue(object : Callback<Training> {
                override fun onResponse(call: Call<Training>, response: Response<Training>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddTrainingActivity, "Training added successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@AddTrainingActivity, "Failed to add training", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Training>, t: Throwable) {
                    Toast.makeText(this@AddTrainingActivity, "Network error", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "No token found, please login", Toast.LENGTH_SHORT).show()
        }
    }
}

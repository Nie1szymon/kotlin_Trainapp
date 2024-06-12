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

class EditTrainingActivity : AppCompatActivity() {

    private lateinit var editTextTrainingName: EditText
    private lateinit var editTextTrainingDesc: EditText
    private lateinit var buttonSaveTraining: Button
    private var trainingId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_training)

        editTextTrainingName = findViewById(R.id.editTextTrainingName)
        editTextTrainingDesc = findViewById(R.id.editTextTrainingDesc)
        buttonSaveTraining = findViewById(R.id.buttonSaveTraining)

        // Pobierz dane treningu z Intent
        trainingId = intent.getIntExtra("TRAINING_ID", -1)
        val trainingName = intent.getStringExtra("TRAINING_NAME")
        val trainingDesc = intent.getStringExtra("TRAINING_DESC")

        if (trainingId != -1) {
            editTextTrainingName.setText(trainingName)
            editTextTrainingDesc.setText(trainingDesc)
        }

        buttonSaveTraining.setOnClickListener {
            saveTrainingDetails()
        }
    }

    private fun saveTrainingDetails() {
        val name = editTextTrainingName.text.toString()
        val desc = editTextTrainingDesc.text.toString()

        val apiService = RetrofitClient.instance
        val training = Training(id = trainingId, name = name, desc = desc, date = "")

        apiService.updateTraining(trainingId, training).enqueue(object : Callback<Training> {
            override fun onResponse(call: Call<Training>, response: Response<Training>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EditTrainingActivity, "Training updated successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@EditTrainingActivity, "Failed to update training", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Training>, t: Throwable) {
                Toast.makeText(this@EditTrainingActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

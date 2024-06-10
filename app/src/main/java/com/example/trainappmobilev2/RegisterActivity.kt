package com.example.trainappmobilev2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trainappmobilev2.R
import com.example.trainappmobilev2.model.RegisterRequest
import com.example.trainappmobilev2.model.RegisterResponse
import com.example.trainappmobilev2.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var password1EditText: EditText
    private lateinit var password2EditText: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        usernameEditText = findViewById(R.id.usernameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        password1EditText = findViewById(R.id.password1EditText)
        password2EditText = findViewById(R.id.password2EditText)
        registerButton = findViewById(R.id.registerButton)

        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password1 = password1EditText.text.toString()
            val password2 = password2EditText.text.toString()
            if (username.isNotEmpty() && email.isNotEmpty() && password1.isNotEmpty() && password2.isNotEmpty()) {
                register(username, email, password1, password2)
            } else {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun register(username: String, email: String, password1: String, password2: String) {
        val apiService = RetrofitClient.instance
        val registerRequest = RegisterRequest(username, email, password1, password2)
        val call = apiService.register(registerRequest)

        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    registerResponse?.let {
                        if (it.success) {
                            Toast.makeText(this@RegisterActivity, "Registration successful", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@RegisterActivity, "Registration failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Log.e("RegisterActivity", "Registration failed: ${response.errorBody()?.string()}")
                    Toast.makeText(this@RegisterActivity, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e("RegisterActivity", "Network error", t)
                Toast.makeText(this@RegisterActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
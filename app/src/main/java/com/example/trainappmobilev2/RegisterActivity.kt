package com.example.trainappmobilev2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.trainappmobilev2.network.RegisterRequest
import com.example.trainappmobilev2.network.RegisterResponse
import com.example.trainappmobilev2.network.RetrofitClient
import java.io.IOException

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val usernameEditText = findViewById<EditText>(R.id.username)
        val emailEditText = findViewById<EditText>(R.id.email)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val passwordConfirmEditText = findViewById<EditText>(R.id.password_confirm)
        val registerButton = findViewById<Button>(R.id.register_button)

        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val passwordConfirm = passwordConfirmEditText.text.toString()

            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && password == passwordConfirm) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        register(username, email, password, passwordConfirm)
                    } catch (e: IOException) {
                        Log.e("RegisterActivity", "Failed to register", e)
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(this@RegisterActivity, "Failed to register", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Please fill in all fields and make sure passwords match", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun register(username: String, email: String, password: String, passwordConfirm: String) {
        val registerRequest = RegisterRequest(username, email, password, passwordConfirm)
        Log.d("RegisterActivity", "RegisterRequest: $registerRequest")
        RetrofitClient.instance.register(registerRequest).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "Registered successfully!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("RegisterActivity", "Registration failed: ${response.code()} - ${response.message()}")
                    Toast.makeText(this@RegisterActivity, "Registration failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e("RegisterActivity", "Network error: ${t.localizedMessage}", t)
                Toast.makeText(this@RegisterActivity, "Network error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
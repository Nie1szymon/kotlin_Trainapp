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
import com.example.trainappmobilev2.network.LoginRequest
import com.example.trainappmobilev2.network.LoginResponse
import com.example.trainappmobilev2.network.RetrofitClient
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameEditText = findViewById<EditText>(R.id.username)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.login_button)
        val registerButton = findViewById<Button>(R.id.register_button)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        RetrofitClient.fetchCsrfToken()
                        login(username, password)
                    } catch (e: IOException) {
                        Log.e("LoginActivity", "Failed to fetch CSRF token", e)
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(this@LoginActivity, "Failed to fetch CSRF token", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Username and password must not be empty", Toast.LENGTH_SHORT).show()
            }
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login(username: String, password: String) {
        val loginRequest = LoginRequest(username, password)
        RetrofitClient.instance.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    Toast.makeText(this@LoginActivity, "Logged in successfully!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("LoginActivity", "Login failed: ${response.code()} - ${response.message()}")
                    Toast.makeText(this@LoginActivity, "Login failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LoginActivity", "Network error: ${t.localizedMessage}", t)
                Toast.makeText(this@LoginActivity, "Network error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
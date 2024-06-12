package com.example.trainappmobilev2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Retrieve FCM token
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("FCM Token", token)
            } else {
                Log.w("FCM Token", "Fetching FCM token failed", task.exception)
            }
        }

        // Initialize buttons and their click listeners
        setupButtons()
    }

    private fun setupButtons() {
        val plansButton: Button = findViewById(R.id.plansButton)
        plansButton.setOnClickListener {
            val intent = Intent(this, PlansActivity::class.java)
            startActivity(intent)
        }

        val userPlansButton: Button = findViewById(R.id.userPlansButton)
        userPlansButton.setOnClickListener {
            val intent = Intent(this, UserPlansActivity::class.java)
            startActivity(intent)
        }

        val editPlanButton: Button = findViewById(R.id.editPlanButton)
        editPlanButton.setOnClickListener {
            val intent = Intent(this, EditPlansActivity::class.java)
            startActivity(intent)
        }
    }
}

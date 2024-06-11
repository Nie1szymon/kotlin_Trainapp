package com.example.trainappmobilev2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.trainappmobilev2.PlansActivity
import com.example.trainappmobilev2.R
import com.example.trainappmobilev2.UserPlansActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Znajdź przycisk i ustaw listener kliknięcia dla PlansActivity
        val plansButton: Button = findViewById(R.id.plansButton)
        plansButton.setOnClickListener {
            val intent = Intent(this, PlansActivity::class.java)
            startActivity(intent)
        }

        // Znajdź przycisk i ustaw listener kliknięcia dla UserPlansActivity
        val userPlansButton: Button = findViewById(R.id.userPlansButton)
        userPlansButton.setOnClickListener {
            val intent = Intent(this, UserPlansActivity::class.java)
            startActivity(intent)
        }
    }
}
package com.example.workoutapp.ViewModel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.workoutapp.R
import com.google.firebase.auth.FirebaseAuth

class HomepageActivity : AppCompatActivity() {
    private lateinit var logoutButton: Button
    private lateinit var auth: FirebaseAuth

    private lateinit var welcomeTextView: TextView // New
    private lateinit var fitnessInfoTextView: TextView // New

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        auth = FirebaseAuth.getInstance()
        logoutButton = findViewById(R.id.button3)
        welcomeTextView = findViewById(R.id.welcomeTextView) // Update with your TextView ID
        fitnessInfoTextView = findViewById(R.id.fitnessInfoTextView) // Update with your TextView ID

        logoutButton.setOnClickListener {
            logOut()
        }

        // Fetch and display survey data
    }

    private fun logOut() {
        // Sign out the user
        auth.signOut()

        // Navigate back to the login screen
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }


        }

package com.example.workoutapp.ViewModel

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.workoutapp.Model.Workout
import com.example.workoutapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class WorkoutDetails : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_details)

        // Retrieve the full Workout object passed from the adapter
        val workout = intent.getSerializableExtra("workoutDetails") as? Workout
        if (workout == null) {
            finish()
            return
        }

        val txt_name: TextView = findViewById(R.id.txt_name)
        val txt_type: TextView = findViewById(R.id.txt_type)
        val txt_duration: TextView = findViewById(R.id.txt_duration)
        val txt_rest: TextView = findViewById(R.id.txt_rest)
        val txt_exercise1: TextView = findViewById(R.id.txt_exercise_1)
        val txt_exercise2: TextView = findViewById(R.id.txt_exercise_2)
        val txt_exercise3: TextView = findViewById(R.id.txt_exercise_3)
        val txt_exercise4: TextView = findViewById(R.id.txt_exercise_4)
        val finishButton: Button = findViewById(R.id.btn_finish)

        // Populate the elements with workout data
        txt_name.text = "Name: ${workout.name}"
        txt_type.text = "Type: ${workout.type}"
        txt_duration.text = "Duration: ${workout.duration}"
        txt_rest.text = "Rest: ${workout.restbetweensets}"
        txt_exercise1.text = "Exercise 1: ${workout.exercise1}"
        txt_exercise2.text = "Exercise 2: ${workout.exercise2}"
        txt_exercise3.text = "Exercise 3: ${workout.exercise3}"
        txt_exercise4.text = "Exercise 4: ${workout.exercise4}"

        finishButton.setOnClickListener {
            saveWorkoutData(workout)
        }
    }

    private fun saveWorkoutData(workout: Workout) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val db = FirebaseFirestore.getInstance()

            // Get the current date
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            val workoutData = hashMapOf(
                "workoutName" to workout.name,
                "date" to date
            )

            // Save the workout data in Firestore under the user's workoutHistory collection
            db.collection("users")
                .document(userId)
                .collection("workoutHistory")
                .add(workoutData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Workout saved successfully!", Toast.LENGTH_SHORT).show()
                    navigateToHistory()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error saving workout: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }


    private fun navigateToHistory() {
        val historyFragment = HistoryActivity.newInstance()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content1, historyFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }



}

package com.example.workoutapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SurveyActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var submitButton: Button
    private var answers = mutableMapOf<Int, String>()

    private var currentStage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey)

        recyclerView = findViewById(R.id.surveyRecyclerView)
        submitButton = findViewById(R.id.submitSurveyButton)

        setupSurvey(currentStage)

        submitButton.setOnClickListener {
            if (currentStage == 1) {
                // Move to the next stage
                currentStage = 2
                setupSurvey(currentStage)
            } else {
                // Save or submit data here
                saveAnswers()
                // Redirect or finish
                navigateToHomeScreen()
            }
        }
    }

    private fun setupSurvey(stage: Int) {
        val questions = when (stage) {
            1 -> listOf(
                SurveyAdapter.Question("First Name"),
                SurveyAdapter.Question("Last Name"),
                SurveyAdapter.Question("Age"),
                SurveyAdapter.Question("Weight"),
                SurveyAdapter.Question("Height")
            )
            2 -> listOf(
                SurveyAdapter.Question("Fitness Goals", listOf("Lose Weight", "Build Muscle", "Maintain Fitness")),
                SurveyAdapter.Question(
                    "Preferred Workout Types",
                    listOf("Cardio", "Strength Training", "Bodybuilding", "HIIT")
                ),
                SurveyAdapter.Question(
                    "Workout Duration",
                    listOf("15-30 mins", "30-45 mins", "45-60 mins")
                ),
                SurveyAdapter.Question(
                    "Workout Frequency",
                    listOf("2 times a week", "3 times a week", "4 times a week", "5+ times a week")
                )
            )
            else -> emptyList()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SurveyAdapter(questions) { position, answer ->
            answers[position] = answer
        }
    }

    private fun navigateToHomeScreen() {
        val intent = Intent(this, HomepageActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun saveAnswers() {
        // Save answers to RoomDB or send to backend
    }
}

package com.example.workoutapp.Model

data class SurveyQuestion(
    val question: String,
    val inputType: Int, // Use constants for types (e.g., TEXT, DROPDOWN)
    val options: List<String>? = null // For dropdown/spinner
)


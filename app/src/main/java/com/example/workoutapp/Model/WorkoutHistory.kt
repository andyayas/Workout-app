package com.example.workoutapp.Model

import java.io.Serializable

data class WorkoutHistory(
    val workoutName: String = "",
    val workoutDate: String = ""
) : Serializable

package com.example.workoutapp.Model

import com.google.firebase.firestore.PropertyName
import java.io.Serializable

data class Workout(
    var name: String? = null,
    var type: String? = null,
    var duration: String? = null,
    var restbetweensets: String? = null,

    @PropertyName("exercise1") var exercise1: String? = null,
    @PropertyName("exercise2") var exercise2: String? = null,
    @PropertyName("exercise3") var exercise3: String? = null,
    @PropertyName("exercise4") var exercise4: String? = null
) : Serializable

package com.example.workoutapp.ViewModel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.workoutapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : Fragment() {

    private lateinit var userNameTextView: TextView
    private lateinit var surveyDataTextView: TextView
    private lateinit var editProfileButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_page, container, false)

        userNameTextView = view.findViewById(R.id.userNameTextView)
        surveyDataTextView = view.findViewById(R.id.surveyDataTextView)
        editProfileButton = view.findViewById(R.id.editProfileButton)

        displayUserInfo()

        editProfileButton.setOnClickListener {
            openFragment(EditProfileActivity())
        }

        return view
    }

    private fun displayUserInfo() {
        val user = FirebaseAuth.getInstance().currentUser

        user?.let {
            val db = FirebaseFirestore.getInstance()

            // Get the user's survey data from the correct path
            val surveyDataRef = db.collection("users")
                .document(user.uid)
                .collection("survey")
                .document("userSurveyData")

            surveyDataRef.get().addOnSuccessListener { surveyDocument ->
                if (surveyDocument.exists()) {
                    // Retrieve the First Name from the survey data
                    val firstName = surveyDocument.getString("First Name") ?: "No First Name"

                    userNameTextView.text = firstName

                    // Build a string from the survey data
                    val questionnaireData = buildString {
                        // Define the order of the questions
                        val questionOrder = listOf(
                            "First Name",
                            "Last Name",
                            "Age",
                            "Weight",
                            "Height",
                            "Fitness Goals",
                            "Preferred Workout Types",
                            "Workout Duration",
                            "Workout Frequency"
                        )

                        questionOrder.forEach { key ->
                            val answer = surveyDocument.getString(key) ?: "No answer"
                            append("$key: $answer\n")  // Format as key: value
                        }
                    }
                    surveyDataTextView.text = questionnaireData
                } else {
                    surveyDataTextView.text = "No survey data found."
                }
            }.addOnFailureListener { e ->
                Toast.makeText(context, "Error fetching survey data.", Toast.LENGTH_SHORT).show()
            }

        } ?: run {
            Toast.makeText(context, "User not logged in.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.content1, fragment)  // Replace the current fragment
        transaction.addToBackStack(null)  // Add to back stack for navigation
        transaction.commit()
    }

    companion object {
        fun newInstance(): ProfileActivity = ProfileActivity()
    }
}

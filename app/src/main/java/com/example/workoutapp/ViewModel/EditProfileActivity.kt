package com.example.workoutapp.ViewModel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.workoutapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : Fragment() {

    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var weightEditText: EditText
    private lateinit var heightEditText: EditText
    private lateinit var goalsSpinner: Spinner
    private lateinit var typesSpinner: Spinner
    private lateinit var durationSpinner: Spinner
    private lateinit var frequencySpinner: Spinner
    private lateinit var saveButton: Button

    private lateinit var db: FirebaseFirestore
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        // Initialize views
        firstNameEditText = view.findViewById(R.id.firstNameEditText)
        lastNameEditText = view.findViewById(R.id.lastNameEditText)
        ageEditText = view.findViewById(R.id.ageEditText)
        weightEditText = view.findViewById(R.id.weightEditText)
        heightEditText = view.findViewById(R.id.heightEditText)
        goalsSpinner = view.findViewById(R.id.goalsSpinner)
        typesSpinner = view.findViewById(R.id.typesSpinner)
        durationSpinner = view.findViewById(R.id.durationSpinner)
        frequencySpinner = view.findViewById(R.id.frequencySpinner)
        saveButton = view.findViewById(R.id.saveButton)

        db = FirebaseFirestore.getInstance()

        // Populate spinners
        populateSpinners()

        // Load user data
        loadUserData()

        // Save updated data
        saveButton.setOnClickListener { saveUserData() }

        return view
    }

    private fun populateSpinners() {
        // Spinner options
        val fitnessGoals = listOf("Lose Weight", "Build Muscle", "Maintain Fitness")
        val workoutTypes = listOf("Cardio", "Strength Training", "Bodybuilding", "HIIT")
        val durations = listOf("15-30 mins", "30-45 mins", "45-60 mins")
        val frequencies = listOf("2 times a week", "3 times a week", "4 times a week", "5+ times a week")

        // Set spinners with the data
        goalsSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, fitnessGoals)
        typesSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, workoutTypes)
        durationSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, durations)
        frequencySpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, frequencies)
    }

    private fun loadUserData() {
        userId?.let {
            db.collection("users").document(it)
                .get()
                .addOnSuccessListener { userDocument ->
                    if (userDocument.exists()) {
                        // Populate the fields with user data
                        firstNameEditText.setText(userDocument.getString("First Name"))
                        lastNameEditText.setText(userDocument.getString("Last Name"))
                        ageEditText.setText(userDocument.getString("Age"))
                        weightEditText.setText(userDocument.getString("Weight"))
                        heightEditText.setText(userDocument.getString("Height"))

                        // Populate the spinners based on user data
                        // Options for spinners
                        val fitnessGoals = listOf("Lose Weight", "Build Muscle", "Maintain Fitness")
                        val workoutTypes = listOf("Cardio", "Strength Training", "Bodybuilding", "HIIT")
                        val durations = listOf("15-30 mins", "30-45 mins", "45-60 mins")
                        val frequencies = listOf("2 times a week", "3 times a week", "4 times a week", "5+ times a week")

                        // Check if userDocument.data is not null
                        userDocument.data?.let { data ->
                            // Values for spinners based on the user's current data
                            (data["Fitness Goals"] as? String)?.let { goalsSpinner.setSelection(fitnessGoals.indexOf(it)) }
                            (data["Preferred Workout Types"] as? String)?.let { typesSpinner.setSelection(workoutTypes.indexOf(it)) }
                            (data["Workout Duration"] as? String)?.let { durationSpinner.setSelection(durations.indexOf(it)) }
                            (data["Workout Frequency"] as? String)?.let { frequencySpinner.setSelection(frequencies.indexOf(it)) }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Failed to load profile data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }


    private fun saveUserData() {
        val userData = mapOf(
            "First Name" to firstNameEditText.text.toString(),
            "Last Name" to lastNameEditText.text.toString(),
            "Age" to ageEditText.text.toString(),
            "Weight" to weightEditText.text.toString(),
            "Height" to heightEditText.text.toString(),
            "Fitness Goals" to goalsSpinner.selectedItem.toString(),
            "Preferred Workout Types" to typesSpinner.selectedItem.toString(),
            "Workout Duration" to durationSpinner.selectedItem.toString(),
            "Workout Frequency" to frequencySpinner.selectedItem.toString()
        )

        userId?.let {
            val userDocRef = db.collection("users")
                .document(it)  // User document
                .collection("survey")  // Subcollection
                .document("userSurveyData")  // Document containing survey data

            // set() to overwrite the document or update
            userDocRef.set(userData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()

                    // Navigate to the Profile Fragment after saving
                    navigateToProfilePage()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Failed to update profile: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
    private fun navigateToProfilePage() {
        // Create the ProfileFragment instance
        val profileFragment = ProfileActivity.newInstance()

        // Start the transaction
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.content1, profileFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}
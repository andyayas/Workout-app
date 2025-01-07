package com.example.workoutapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapp.Adapter.WorkoutAdapter
import com.example.workoutapp.Model.Workout
import com.example.workoutapp.ViewModel.LoginActivity
import com.example.workoutapp.ViewModel.WorkoutActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeScreenActivity : Fragment() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private var recyclerView: RecyclerView? = null
    private var adapter: WorkoutAdapter? = null
    private var user: FirebaseUser? = null
    private var workoutList = mutableListOf<Workout>()
    private lateinit var db: FirebaseFirestore
    private lateinit var name: TextView
    private lateinit var logoutButton: ImageView

    private fun getData() {
        db.collection("workouts")
            .get()
            .addOnSuccessListener { documents: QuerySnapshot ->
                workoutList.clear()
                for (document in documents) {
                    val workout = document.toObject(Workout::class.java)
                    workoutList.add(workout)
                }
                adapter = WorkoutAdapter(workoutList, requireContext())
                recyclerView?.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting documents: ", exception)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)
        linearLayoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = linearLayoutManager
        recyclerView?.setHasFixedSize(true)

        db = Firebase.firestore
        user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            getData()
            fetchAndDisplayUserName()
        }

        name = view.findViewById(R.id.name)
        logoutButton = view.findViewById(R.id.logoutButton)

        // Handle logout
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val loginIntent = Intent(context, LoginActivity::class.java)
            startActivity(loginIntent)
            activity?.finish()
        }

        // Handle start workout button click
        val startWorkoutButton: Button = view.findViewById(R.id.startworkoutButton)
        startWorkoutButton.setOnClickListener {
            val fragment = WorkoutActivity()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.content1, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return view
    }

    private fun fetchAndDisplayUserName() {
        val userId = user?.uid ?: return

        // Access the Firestore collection and document where user data is stored
        db.collection("users")
            .document(userId)
            .collection("survey")
            .document("userSurveyData")
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val firstName = document.getString("First Name") ?: "User"
                    name.text = "Welcome, $firstName"
                } else {
                    name.text = "Welcome, User"
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching user name: ", exception)
                name.text = "Welcome, User"
            }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.content1, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    companion object {
        fun newInstance(): HomeScreenActivity = HomeScreenActivity()
    }
}

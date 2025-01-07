package com.example.workoutapp.ViewModel

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workoutapp.Model.WorkoutHistory
import com.example.workoutapp.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import androidx.recyclerview.widget.RecyclerView

class HistoryActivity : Fragment(R.layout.fragment_history) {

    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var workoutHistoryList: MutableList<WorkoutHistory>
    private lateinit var historyAdapter: WorkoutHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("HistoryFragment", "onCreateView called")

        val view = inflater.inflate(R.layout.fragment_history, container, false)

        // Initialize the RecyclerView
        historyRecyclerView = view.findViewById(R.id.historyRecyclerView)
        workoutHistoryList = mutableListOf()

        // Set up the RecyclerView layout manager and adapter
        historyRecyclerView.layoutManager = LinearLayoutManager(context)
        historyAdapter = WorkoutHistoryAdapter(workoutHistoryList)
        historyRecyclerView.adapter = historyAdapter

        // Fetch the workout history from Firestore
        fetchWorkoutHistory()

        return view
    }

    private fun fetchWorkoutHistory() {
        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()

        // Fetch workout history from Firestore
        user?.let { firebaseUser ->
            val workoutHistoryRef = db.collection("users")
                .document(firebaseUser.uid)
                .collection("workoutHistory") // Subcollection to store workout history

            workoutHistoryRef.get()
                .addOnSuccessListener { result ->
                    if (!result.isEmpty) {
                        workoutHistoryList.clear()
                        for (document in result) {
                            val workoutName = document.getString("workoutName") ?: "No Name"
                            val workoutDate = document.getString("date") ?: "No Date"

                            // Add data to list
                            workoutHistoryList.add(WorkoutHistory(workoutName, workoutDate))
                        }
                        historyAdapter.notifyDataSetChanged() // Notify adapter about data change
                    } else {
                        Toast.makeText(context, "No workout history found.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error fetching workout history: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } ?: run {
            Toast.makeText(context, "User not logged in.", Toast.LENGTH_SHORT).show()
        }

    }
    companion object {
        fun newInstance(): HistoryActivity = HistoryActivity()
    }
}

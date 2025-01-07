package com.example.workoutapp.ViewModel

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapp.Adapter.WorkoutAdapter // Use your updated adapter
import com.example.workoutapp.Model.Workout // Use your Workout model class
import com.example.workoutapp.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class WorkoutActivity : Fragment() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private var recyclerView: RecyclerView? = null
    private var adapter: WorkoutAdapter? = null
    private var workoutList: MutableList<Workout> = mutableListOf()
    private var db: FirebaseFirestore? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_workout_page, container, false)

        linearLayoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = linearLayoutManager
        recyclerView?.setHasFixedSize(true)

        adapter = WorkoutAdapter(workoutList, requireContext())
        recyclerView?.adapter = adapter

        db = FirebaseFirestore.getInstance()
        getData()

        return view
    }

    private fun getData() {
        db!!.collection("workouts")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    workoutList.clear()
                    for (document in task.result!!) {
                        val workout = document.toObject(Workout::class.java)
                        workoutList.add(workout)
                    }
                    adapter!!.notifyDataSetChanged()
                } else {
                    Log.w("Firestore", "Error getting documents.", task.exception)
                }
            }
    }

    companion object {
        fun newInstance(): WorkoutActivity = WorkoutActivity()
    }
}


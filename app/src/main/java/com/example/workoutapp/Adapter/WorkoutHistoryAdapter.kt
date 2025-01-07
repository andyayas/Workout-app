package com.example.workoutapp.ViewModel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapp.Model.WorkoutHistory
import com.example.workoutapp.R

class WorkoutHistoryAdapter(private val workoutHistoryList: List<WorkoutHistory>) :
    RecyclerView.Adapter<WorkoutHistoryAdapter.WorkoutHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.workout_history_item, parent, false)
        return WorkoutHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutHistoryViewHolder, position: Int) {
        val workoutHistory = workoutHistoryList[position]
        holder.workoutNameTextView.text = workoutHistory.workoutName
        holder.workoutDateTextView.text = workoutHistory.workoutDate
    }

    override fun getItemCount(): Int = workoutHistoryList.size

    inner class WorkoutHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val workoutNameTextView: TextView = itemView.findViewById(R.id.workoutNameTextView)
        val workoutDateTextView: TextView = itemView.findViewById(R.id.workoutDateTextView)
    }
}

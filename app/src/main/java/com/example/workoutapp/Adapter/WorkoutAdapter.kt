package com.example.workoutapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapp.R
import com.example.workoutapp.Model.Workout
import com.example.workoutapp.ViewModel.WorkoutDetails

class WorkoutAdapter(private val workoutList: MutableList<Workout>, val context: Context) : RecyclerView.Adapter<WorkoutAdapter.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_workout, parent, false)
        return MyHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val currentItem = workoutList[position]
        holder.txt_workoutName.text = currentItem.name
        holder.txt_workoutType.text = "Type: ${currentItem.type}"
        holder.txt_duration.text = "Duration: ${currentItem.duration}"


        holder.btn_startWorkout.setOnClickListener {
            val intent = Intent(context, WorkoutDetails::class.java)
            // Pass the full Workout object using putExtra
            intent.putExtra("workoutDetails", currentItem) // currentItem contains all the workout data, including exercises
            context.startActivity(intent)
        }

    }

    override fun getItemCount() = workoutList.size

    class MyHolder(v: View) : RecyclerView.ViewHolder(v) {
        var txt_workoutName: TextView = v.findViewById(R.id.txt_name)
        var txt_workoutType: TextView = v.findViewById(R.id.txt_type)
        var txt_duration: TextView = v.findViewById(R.id.txt_duration)
        var btn_startWorkout: Button = v.findViewById(R.id.btn_start)
    }
}

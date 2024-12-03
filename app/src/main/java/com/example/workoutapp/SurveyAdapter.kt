package com.example.workoutapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SurveyAdapter(
    private val questions: List<Question>,
    private val onAnswerSelected: (Int, String) -> Unit
) : RecyclerView.Adapter<SurveyAdapter.SurveyViewHolder>() {

    data class Question(val text: String, val options: List<String>? = null)

    class SurveyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val questionText: TextView = view.findViewById(R.id.survey_question_text)
        val answerInput: EditText = view.findViewById(R.id.survey_answer_input)
        val optionsGroup: RadioGroup = view.findViewById(R.id.survey_options_group)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_survey_item, parent, false)
        return SurveyViewHolder(view)
    }

    override fun onBindViewHolder(holder: SurveyViewHolder, position: Int) {
        val question = questions[position]
        holder.questionText.text = question.text

        if (question.options.isNullOrEmpty()) {
            // Show input field for free-text questions
            holder.answerInput.visibility = View.VISIBLE
            holder.optionsGroup.visibility = View.GONE
            holder.answerInput.setOnFocusChangeListener { _, _ ->
                onAnswerSelected(position, holder.answerInput.text.toString())
            }
        } else {
            // Show radio buttons for multiple-choice questions
            holder.answerInput.visibility = View.GONE
            holder.optionsGroup.visibility = View.VISIBLE
            holder.optionsGroup.removeAllViews()

            question.options.forEach { option ->
                val radioButton = RadioButton(holder.itemView.context).apply {
                    text = option
                }
                holder.optionsGroup.addView(radioButton)
            }

            holder.optionsGroup.setOnCheckedChangeListener { _, checkedId ->
                val selectedOption = holder.optionsGroup.findViewById<RadioButton>(checkedId)?.text.toString()
                onAnswerSelected(position, selectedOption)
            }
        }
    }

    override fun getItemCount(): Int = questions.size
}

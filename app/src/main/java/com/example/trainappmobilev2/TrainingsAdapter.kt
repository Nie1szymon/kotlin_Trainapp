package com.example.trainappmobilev2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trainappmobilev2.model.Training

class TrainingsAdapter(
    private val trainings: List<Training>,
    private val buttonText: String,
    private val onButtonClick: (Training, Int) -> Unit,
    private val planId: Int
) : RecyclerView.Adapter<TrainingsAdapter.TrainingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_training, parent, false)
        return TrainingViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int) {
        holder.bind(trainings[position], buttonText, onButtonClick, planId)
    }

    override fun getItemCount(): Int = trainings.size

    class TrainingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val trainingNameTextView: TextView = itemView.findViewById(R.id.trainingNameTextView)
        private val trainingDescTextView: TextView = itemView.findViewById(R.id.trainingDescTextView)
        private val goToExercisesButton: Button = itemView.findViewById(R.id.goToExercisesButton)

        fun bind(training: Training, buttonText: String, onButtonClick: (Training, Int) -> Unit, planId: Int) {
            trainingNameTextView.text = training.name
            trainingDescTextView.text = training.desc
            goToExercisesButton.text = buttonText
            goToExercisesButton.setOnClickListener { onButtonClick(training, planId) }
        }
    }
}


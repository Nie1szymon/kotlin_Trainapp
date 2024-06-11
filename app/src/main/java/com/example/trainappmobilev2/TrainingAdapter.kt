package com.example.trainappmobilev2

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trainappmobilev2.R
import com.example.trainappmobilev2.model.Training
import com.example.trainappmobilev2.ExercisesActivity

class TrainingAdapter : RecyclerView.Adapter<TrainingAdapter.TrainingViewHolder>() {

    private var trainings: List<Training> = listOf()

    fun setTrainings(trainings: List<Training>) {
        this.trainings = trainings
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_training, parent, false)
        return TrainingViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int) {
        holder.bind(trainings[position])
    }

    override fun getItemCount(): Int = trainings.size

    class TrainingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val trainingNameTextView: TextView = itemView.findViewById(R.id.trainingNameTextView)
        private val trainingDescTextView: TextView = itemView.findViewById(R.id.trainingDescTextView)
        private val goToExercisesButton: Button = itemView.findViewById(R.id.goToExercisesButton)

        fun bind(training: Training) {
            trainingNameTextView.text = training.name
            trainingDescTextView.text = training.desc
            goToExercisesButton.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, ExercisesActivity::class.java)
                intent.putExtra("TRAINING_ID", training.id)
                context.startActivity(intent)
            }
        }
    }
}
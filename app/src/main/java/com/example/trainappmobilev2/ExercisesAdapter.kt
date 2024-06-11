package com.example.trainappmobilev2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trainappmobilev2.R
import com.example.trainappmobilev2.model.TrainingsExercises

class ExercisesAdapter : RecyclerView.Adapter<ExercisesAdapter.ExerciseViewHolder>() {

    private var exercises: List<TrainingsExercises> = listOf()

    fun setExercises(exercises: List<TrainingsExercises>) {
        this.exercises = exercises
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.bind(exercises[position])
    }

    override fun getItemCount(): Int = exercises.size

    class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val exerciseNameTextView: TextView = itemView.findViewById(R.id.exerciseNameTextView)
        private val exerciseDescTextView: TextView = itemView.findViewById(R.id.exerciseDescTextView)
        private val seriesTextView: TextView = itemView.findViewById(R.id.seriesTextView)
        private val repeatTextView: TextView = itemView.findViewById(R.id.repeatTextView)

        fun bind(trainingExercise: TrainingsExercises) {
            exerciseNameTextView.text = trainingExercise.exercises_name
            exerciseDescTextView.text = trainingExercise.exercises_desc
            seriesTextView.text = "Series: ${trainingExercise.series}"
            repeatTextView.text = "Repeat: ${trainingExercise.repeat}"
        }
    }
}

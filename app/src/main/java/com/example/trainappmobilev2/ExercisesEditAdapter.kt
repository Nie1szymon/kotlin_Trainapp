package com.example.trainappmobilev2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trainappmobilev2.model.TrainingsExercises

class ExercisesEditAdapter(
    private val exercises: List<TrainingsExercises>,
    private val onEditClick: (TrainingsExercises) -> Unit,
    private val onDeleteClick: (TrainingsExercises) -> Unit
) : RecyclerView.Adapter<ExercisesEditAdapter.ExerciseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_edit_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.bind(exercises[position])
    }

    override fun getItemCount(): Int = exercises.size

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val exerciseNameTextView: TextView = itemView.findViewById(R.id.exerciseNameTextView)
        private val seriesEditText: EditText = itemView.findViewById(R.id.seriesEditText)
        private val repeatEditText: EditText = itemView.findViewById(R.id.repeatEditText)
        private val editButton: Button = itemView.findViewById(R.id.editButton)
        private val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

        fun bind(trainingExercise: TrainingsExercises) {
            exerciseNameTextView.text = trainingExercise.exercises_name // Zakładając, że exercises ma pole name
            seriesEditText.setText(trainingExercise.series.toString())
            repeatEditText.setText(trainingExercise.repeat.toString())

            editButton.setOnClickListener {
                trainingExercise.series = seriesEditText.text.toString().toInt()
                trainingExercise.repeat = repeatEditText.text.toString().toInt()
                onEditClick(trainingExercise)
            }

            deleteButton.setOnClickListener {
                onDeleteClick(trainingExercise)
            }
        }
    }
}




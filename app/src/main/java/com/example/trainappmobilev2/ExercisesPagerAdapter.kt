package com.example.trainappmobilev2

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trainappmobilev2.R
import com.example.trainappmobilev2.model.PixabayImageResponse
import com.example.trainappmobilev2.model.TrainingsExercises
import com.example.trainappmobilev2.network.PixabayRetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExercisesPagerAdapter(private val exercises: List<TrainingsExercises>) :
    RecyclerView.Adapter<ExercisesPagerAdapter.ExerciseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exercise_page, parent, false)
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
        private val exerciseImageView: ImageView = itemView.findViewById(R.id.exerciseImageView)

        fun bind(trainingExercise: TrainingsExercises) {
            exerciseNameTextView.text = trainingExercise.exercises_name
            exerciseDescTextView.text = trainingExercise.exercises_desc
            seriesTextView.text = "Series: ${trainingExercise.series}"
            repeatTextView.text = "Repeat: ${trainingExercise.repeat}"

            // Fetch image based on exercise name
            val apiService = PixabayRetrofitClient.apiService
            apiService.searchPixabayImages(trainingExercise.exercises_name).enqueue(object : Callback<PixabayImageResponse> {
                override fun onResponse(call: Call<PixabayImageResponse>, response: Response<PixabayImageResponse>) {
                    if (response.isSuccessful) {
                        val imageUrl = response.body()?.hits?.firstOrNull()?.webformatURL
                        Log.d("ExerciseViewHolder", "Image URL: $imageUrl")
                        imageUrl?.let {
                            Glide.with(itemView.context)
                                .load(it)
                                .into(exerciseImageView)
                        }
                    } else {
                        Log.e("ExerciseViewHolder", "Error: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<PixabayImageResponse>, t: Throwable) {
                    Log.e("ExerciseViewHolder", "Network error", t)
                }
            })
        }
    }
}
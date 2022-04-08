package com.kamikadze328.mtstetaproject.adapter.movieshort

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.dto.Movie


class MovieShortViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val root: ConstraintLayout = view.findViewById(R.id.movie_short_root)
    private val poster: ImageView = view.findViewById(R.id.movie_short_poster)
    private val textView: TextView = view.findViewById(R.id.movie_short_name)

    fun bind(movie: Movie, click: (id: Long) -> Unit) {
        poster.load(movie.poster_path) {
            transformations(RoundedCornersTransformation(view.context.resources.getDimension(R.dimen.movie_main_poster_border_radius)))
        }
        root.setOnClickListener { click(movie.movieId) }
        textView.text = movie.title
    }


    companion object {
        fun from(parent: ViewGroup): MovieShortViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_movie_short, parent, false)
            return MovieShortViewHolder(view)
        }
    }
}
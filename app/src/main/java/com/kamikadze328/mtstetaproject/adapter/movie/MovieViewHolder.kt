package com.kamikadze328.mtstetaproject.adapter.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import coil.load
import coil.transform.RoundedCornersTransformation
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.dto.Movie
import com.kamikadze328.mtstetaproject.setRating


class MovieViewHolder(private val view: View) : MovieViewHolderSealed(view) {
    private val root: ConstraintLayout = view.findViewById(R.id.movie_main_root)
    private val title: TextView = view.findViewById(R.id.movie_main_name)
    private val description: TextView = view.findViewById(R.id.movie_main_description)
    private val rating: ConstraintLayout = view.findViewById(R.id.rating_bar_root)
    private val ageRestriction: TextView = view.findViewById(R.id.movie_main_age_rating)
    private val poster: ImageView = view.findViewById(R.id.movie_main_poster)

    fun bind(movie: Movie, click: (id: Int) -> Unit) {

        poster.load(movie.poster_path) {
            transformations(RoundedCornersTransformation(view.context.resources.getDimension(R.dimen.movie_main_poster_border_radius)))
        }
        root.setOnClickListener { click(movie.id) }
        title.text = movie.title
        description.text = movie.overview
        rating.setRating(movie.vote_average)
        ageRestriction.text = view.context.resources.getString(
            R.string.main_age_restriction_text,
            movie.ageRestriction
        )
    }


    companion object {
        fun from(parent: ViewGroup): MovieViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_movie, parent, false)
            return MovieViewHolder(view)
        }
    }
}
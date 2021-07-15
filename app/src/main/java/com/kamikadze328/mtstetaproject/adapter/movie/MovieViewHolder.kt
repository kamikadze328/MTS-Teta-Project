package com.kamikadze328.mtstetaproject.adapter.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import coil.load
import coil.request.ImageRequest
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

    fun bind(data: Movie, click: (title: String) -> Unit) {

        poster.load(data.imageUrl) {
            transformations(RoundedCornersTransformation(view.context.resources.getDimension(R.dimen.movie_main_poster_border_radius)))
        }
        root.setOnClickListener { click(data.title) }
        title.text = data.title
        description.text = data.description
        rating.setRating(data.rateScore)
        ageRestriction.text = view.context.resources.getString(
            R.string.main_age_restriction_text,
            data.ageRestriction
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
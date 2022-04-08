package com.kamikadze328.mtstetaproject.adapter.movieshort

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.kamikadze328.mtstetaproject.adapter.movie.MovieCallback
import com.kamikadze328.mtstetaproject.data.dto.Movie

class MovieShortAdapter(private val click: (id: Long) -> Unit) :
    ListAdapter<Movie, MovieShortViewHolder>(MovieCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MovieShortViewHolder.from(parent)


    override fun onBindViewHolder(holder: MovieShortViewHolder, position: Int) {
        holder.bind(getItem(position), click)
    }
}
package com.kamikadze328.mtstetaproject.adapter.movie

import androidx.recyclerview.widget.DiffUtil
import com.kamikadze328.mtstetaproject.data.dto.Movie

class MovieCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}
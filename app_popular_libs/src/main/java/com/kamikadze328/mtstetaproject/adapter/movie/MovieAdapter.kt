package com.kamikadze328.mtstetaproject.adapter.movie

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.kamikadze328.mtstetaproject.data.dto.Movie

class MovieAdapter(private val clickOnMovie: (id: Long, view: View) -> Unit) :
    ListAdapter<Movie, MovieViewHolderSealed>(MovieCallback()) {

    var isEmpty = false

    companion object {
        private const val TYPE_MOVIE = 0
        private const val TYPE_NO_MOVIES = 3
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolderSealed {
        return when (viewType) {
            TYPE_MOVIE -> MovieViewHolder.from(parent)
            TYPE_NO_MOVIES -> NoMoviesViewHolder.from(parent)
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: MovieViewHolderSealed, position: Int) {
        when (holder) {
            is MovieViewHolder -> holder.bind(getItem(position), clickOnMovie)
            is NoMoviesViewHolder -> return
        }
    }

    override fun getItemCount(): Int = if (currentList.size == 0) 1 else currentList.size

    override fun getItemViewType(position: Int): Int {
        return when {
            isEmpty -> TYPE_NO_MOVIES
            else -> TYPE_MOVIE
        }
    }

}
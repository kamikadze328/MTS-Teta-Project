package com.kamikadze328.mtstetaproject.adapter.movie

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.kamikadze328.mtstetaproject.data.dto.Movie

class MovieAdapter(private val clickOnMovie: (id: Int) -> Unit, private val title: String) :
    ListAdapter<Movie, MovieViewHolderSealed>(MovieCallback()) {

    companion object {
        private const val TYPE_MOVIE = 0
        private const val TYPE_HEADER = 1
        private const val TYPE_FOOTER = 2
        private const val TYPE_NO_MOVIES = 3
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolderSealed {
        return when (viewType) {
            TYPE_MOVIE -> MovieViewHolder.from(parent)
            TYPE_FOOTER -> FooterViewHolder.from(parent)
            TYPE_HEADER -> HeaderViewHolder.from(parent)
            TYPE_NO_MOVIES -> NoMoviesViewHolder.from(parent)
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: MovieViewHolderSealed, position: Int) {
        when (holder) {
            is MovieViewHolder -> holder.bind(getItem(position), clickOnMovie)
            is HeaderViewHolder -> holder.bind(title)
            is FooterViewHolder -> return
            is NoMoviesViewHolder -> return
        }
    }

    override fun getItem(position: Int): Movie = super.getItem(position - 1)

    override fun getItemCount(): Int = if (currentList.size == 0) 1 else currentList.size + 2

    override fun getItemViewType(position: Int): Int {
        return when {
            currentList.size == 0 -> TYPE_NO_MOVIES
            position == 0 -> TYPE_HEADER
            position == itemCount - 1 -> TYPE_FOOTER
            else -> TYPE_MOVIE
        }
    }

}
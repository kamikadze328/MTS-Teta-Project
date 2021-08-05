package com.kamikadze328.mtstetaproject.adapter.genre

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.kamikadze328.mtstetaproject.data.dto.Genre


class GenreAdapter(private val click: (id: Int) -> Unit) :
    ListAdapter<Genre, GenreViewHolderSealed>(GenreCallback()) {

    companion object {
        private const val TYPE_GENRE = 0
        private const val TYPE_LOADING = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolderSealed {
        return when (viewType) {
            TYPE_GENRE -> GenreViewHolder.from(parent)
            else -> throw IllegalStateException()
        }
    }


    override fun onBindViewHolder(holder: GenreViewHolderSealed, position: Int) {
        when (holder) {
            is GenreViewHolder -> holder.bind(getItem(position), click)
        }
    }

    override fun getItemCount(): Int = if (currentList.size == 0) 0 else currentList.size

    override fun getItemViewType(position: Int): Int {
        return when {
            else -> TYPE_GENRE
        }
    }
}
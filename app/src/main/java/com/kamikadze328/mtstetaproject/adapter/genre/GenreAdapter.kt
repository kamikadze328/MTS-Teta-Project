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
            TYPE_LOADING -> GenreLoadingViewHolder.from(parent)
            else -> throw IllegalStateException()
        }
    }


    override fun onBindViewHolder(holder: GenreViewHolderSealed, position: Int) {
        when (holder) {
            is GenreViewHolder -> holder.bind(getItem(position), click)
            is GenreLoadingViewHolder -> return
        }
    }

    override fun getItemCount(): Int {
        return if (currentList.size == 0) 1 else currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (currentList.size) {
            0 -> TYPE_LOADING
            else -> TYPE_GENRE
        }
    }
}
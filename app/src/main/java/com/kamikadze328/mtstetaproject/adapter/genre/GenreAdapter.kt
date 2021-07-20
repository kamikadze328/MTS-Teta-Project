package com.kamikadze328.mtstetaproject.adapter.genre

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.kamikadze328.mtstetaproject.data.dto.Genre


class GenreAdapter(private val click: (id: Int) -> Unit) :
    ListAdapter<Genre, GenreViewHolder>(GenreCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        return GenreViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(getItem(position), click)
    }

}
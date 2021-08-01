package com.kamikadze328.mtstetaproject.adapter.genre

import androidx.recyclerview.widget.DiffUtil
import com.kamikadze328.mtstetaproject.data.dto.Genre

class GenreCallback : DiffUtil.ItemCallback<Genre>() {
    override fun areContentsTheSame(oldItem: Genre, newItem: Genre): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: Genre, newItem: Genre): Boolean {
        return oldItem == newItem
    }
}
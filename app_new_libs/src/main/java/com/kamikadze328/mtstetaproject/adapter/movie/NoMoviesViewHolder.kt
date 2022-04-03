package com.kamikadze328.mtstetaproject.adapter.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kamikadze328.mtstetaproject.R

class NoMoviesViewHolder(view: View) : MovieViewHolderSealed(view) {

    companion object {
        fun from(parent: ViewGroup): NoMoviesViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_no_movies, parent, false)
            return NoMoviesViewHolder(view)
        }
    }
}
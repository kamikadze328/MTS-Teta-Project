package com.kamikadze328.mtstetaproject.presentation.main

import androidx.compose.runtime.Stable

@Stable
fun interface CallbackGenreClicked {
    fun onGenreClicked(id: Long)
}
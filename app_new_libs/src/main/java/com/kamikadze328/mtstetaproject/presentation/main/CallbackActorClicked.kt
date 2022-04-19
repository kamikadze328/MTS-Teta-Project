package com.kamikadze328.mtstetaproject.presentation.main

import androidx.compose.runtime.Stable

@Stable
fun interface CallbackActorClicked {
    fun onActorClicked(id: Int)
}
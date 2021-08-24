package com.kamikadze328.mtstetaproject.data.util

sealed class UIState<out T> {
    object LoadingState : UIState<Nothing>()
    data class ErrorState(var exception: Throwable) : UIState<Nothing>()
    data class DataState<T>(var data: T) : UIState<T>()
}
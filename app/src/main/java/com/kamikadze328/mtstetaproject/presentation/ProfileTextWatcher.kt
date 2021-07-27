package com.kamikadze328.mtstetaproject.presentation

import android.text.Editable
import android.text.TextWatcher
import android.util.Log

fun interface ProfileTextWatcher : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) =applyChangedUserToViewModel()

    fun applyChangedUserToViewModel()
}
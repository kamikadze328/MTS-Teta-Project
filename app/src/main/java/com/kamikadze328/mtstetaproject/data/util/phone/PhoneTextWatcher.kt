package com.kamikadze328.mtstetaproject.data.util.phone

import android.text.Editable
import android.text.TextWatcher

fun interface PhoneTextWatcher : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable?) = applyChangedUserToViewModel()

    fun applyChangedUserToViewModel()
}
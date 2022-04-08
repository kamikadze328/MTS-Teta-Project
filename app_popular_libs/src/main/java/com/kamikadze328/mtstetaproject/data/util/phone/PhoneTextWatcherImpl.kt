package com.kamikadze328.mtstetaproject.data.util.phone

import android.text.Editable
import android.widget.EditText

abstract class PhoneTextWatcherImpl(private val editText: EditText) : PhoneTextWatcher {

    override fun afterTextChanged(s: Editable?) {
        if (s != null && s.isNotEmpty()) {
            editText.removeTextChangedListener(this)
            formatPhoneNumber(s, editText)
            editText.addTextChangedListener(this)
        }

        super.afterTextChanged(s)
    }


}
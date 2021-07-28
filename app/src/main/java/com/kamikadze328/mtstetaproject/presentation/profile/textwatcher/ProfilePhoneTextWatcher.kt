package com.kamikadze328.mtstetaproject.presentation.profile.textwatcher

import android.text.Editable
import android.widget.EditText

private const val SPACE = ' '
private const val MINUS = '-'
private const val PLUS = '+'
private const val SEVEN = '7'

fun formatPhoneNumber(s: Editable, editText: EditText) {
    var countDeleted = 0
    for (i in s.indices) {
        val currI = i - countDeleted
        if (s.length == currI) break
        if (s[currI] == SPACE || s[currI] == MINUS || s[currI] == PLUS || currI == 11) {
            s.delete(currI, currI + 1)
            countDeleted++
        }
    }
    if (s.isNotEmpty()) {
        when (s[0]) {
            SEVEN -> {
                s.insert(0, "$PLUS")
                s.insert(2, "$SPACE")
            }
            PLUS -> s.insert(1, "$SEVEN$SPACE")
            else -> s.insert(0, "$PLUS$SEVEN$SPACE")
        }
        if (s.length > 3) {
            if (editText.selectionStart == 3) editText.setSelection(4)
        }
        if (s.length > 6) {
            s.insert(6, SPACE.toString())
            if (editText.selectionStart == 7) editText.setSelection(6)
        }
        if (s.length > 10) {
            s.insert(10, MINUS.toString())
            if (editText.selectionStart == 11) editText.setSelection(10)
        }
        if (s.length > 13) {
            s.insert(13, MINUS.toString())
            if (editText.selectionStart == 14) editText.setSelection(13)
        }
    }
}

abstract class ProfilePhoneTextWatcher(private val editText: EditText) : ProfileTextWatcher {

    override fun afterTextChanged(s: Editable?) {
        if (s != null && s.isNotEmpty()) {
            editText.removeTextChangedListener(this)
            formatPhoneNumber(s, editText)
            editText.addTextChangedListener(this)
        }

        super.afterTextChanged(s)
    }


}


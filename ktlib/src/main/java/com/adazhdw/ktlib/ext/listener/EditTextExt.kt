package com.adazhdw.ktlib.ext.listener

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText


inline fun EditText.addTextWatcher(
    crossinline after: (text: Editable?) -> Unit,
    crossinline before: (s: CharSequence?, start: Int, count: Int, after: Int) -> Unit,
    crossinline on: (s: CharSequence?, start: Int, before: Int, count: Int) -> Unit
) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            after.invoke(s)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            before.invoke(s, start, count, after)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            on.invoke(s, start, before, count)
        }
    })
}

inline fun EditText.onEnterListener(
    crossinline action: () -> Unit
) {
    setOnEditorActionListener { v, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
            action.invoke()
            return@setOnEditorActionListener true
        }
        false
    }
}
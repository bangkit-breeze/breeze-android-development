package com.example.breeze.ui.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.example.breeze.R
import com.google.android.material.textfield.TextInputEditText

class PasswordEditText : TextInputEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }
    private fun init() {
        val message = resources.getString(R.string.password_characters)
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().length < 6) {
                    setError(message, null)
                } else {
                    error = null
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }
}
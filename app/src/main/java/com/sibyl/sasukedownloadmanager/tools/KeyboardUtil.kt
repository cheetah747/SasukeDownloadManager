package com.sibyl.sasukedownloadmanager.tools

import android.content.Context
import android.os.Handler
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * @author Sasuke on 2018/7/23.
 */

fun showKeyboard(editText: EditText) {
    Handler().post {
        (editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .showSoftInput(editText, 0);
    }
}

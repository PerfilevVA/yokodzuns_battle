package com.sibedge.yokodzun.android.utils.managers

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


object KeyboardManager {

    private fun getInputMethodManager(context: Context) =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    fun showAndRequestFocus(view: View?) {
        if (view == null) {
            return
        }
        if (view.requestFocus()) {
            getInputMethodManager(view.context)
                .showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    fun hide() {
        val appActivity = AppActivityConnector.appActivity ?: return
        getInputMethodManager(appActivity)
            .hideSoftInputFromWindow(appActivity.view.windowToken, 0)
    }
}
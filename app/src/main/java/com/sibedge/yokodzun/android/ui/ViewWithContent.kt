package com.sibedge.yokodzun.android.ui

import android.view.View


interface ViewWithContent<C> {

    val view: View

    var data: C?

}
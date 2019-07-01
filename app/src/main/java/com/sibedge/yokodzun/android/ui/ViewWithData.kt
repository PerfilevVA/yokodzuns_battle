package com.sibedge.yokodzun.android.ui

import android.view.View


interface ViewWithData<C> {

    val view: View

    var data: C?

}
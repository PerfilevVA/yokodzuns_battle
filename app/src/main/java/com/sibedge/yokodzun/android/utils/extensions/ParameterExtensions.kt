package com.sibedge.yokodzun.android.utils.extensions

import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.common.data.Parameter
import ru.hnau.androidutils.context_getters.StringGetter


val Parameter.entityNameWithTitle
    get() = StringGetter(R.string.entity_parameter) + " ${description.title}"
package com.sibedge.yokodzun.android.utils.extensions

import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.common.data.Parameter
import com.sibedge.yokodzun.common.data.Yokodzun
import com.sibedge.yokodzun.common.data.battle.Battle
import ru.hnau.androidutils.context_getters.StringGetter


val Parameter.entityNameWithTitle
    get() = StringGetter(R.string.entity_parameter) + " ${description.title}"
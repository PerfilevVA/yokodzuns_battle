package com.sibedge.yokodzun.android.utils.extensions

import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.common.data.Yokodzun
import com.sibedge.yokodzun.common.data.battle.Battle
import ru.hnau.androidutils.context_getters.StringGetter


val Yokodzun.entityNameWithTitle
    get() = StringGetter(R.string.entity_yokodzun) + " ${description.title}"
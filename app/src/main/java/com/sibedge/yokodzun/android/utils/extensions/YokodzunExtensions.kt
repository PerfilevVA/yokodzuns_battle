package com.sibedge.yokodzun.android.utils.extensions

import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.common.data.Team
import ru.hnau.androidutils.context_getters.StringGetter


val Team.entityNameWithTitle
    get() = StringGetter(R.string.entity_team) + " ${description.title}"
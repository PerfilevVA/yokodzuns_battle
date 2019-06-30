package com.sibedge.yokodzun.android.utils.extensions

import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.common.data.battle.Battle
import com.sibedge.yokodzun.common.data.battle.BattleStatus
import com.sibedge.yokodzun.common.data.battle.Section
import ru.hnau.androidutils.context_getters.StringGetter

val Section.entityNameWithTitle
    get() = StringGetter(R.string.entity_section) + " ${description.title}"
package com.sibedge.yokodzun.android.data

import ru.hnau.jutils.cache.AutoCache
import com.sibedge.yokodzun.android.api.API
import com.sibedge.yokodzun.android.data.entity.LocalTestAttempt
import com.sibedge.yokodzun.android.utils.extensions.sortKey


object TestsAttemptsForStudentManager : RTDataManager<List<LocalTestAttempt>>() {

    override suspend fun getValue() = API
        .getTestAttempts()
        .await()
        .map(::LocalTestAttempt)
        .sortedBy { attempt -> attempt.sortKey }

}
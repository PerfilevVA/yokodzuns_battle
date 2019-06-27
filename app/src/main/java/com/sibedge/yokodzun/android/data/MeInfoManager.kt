package com.sibedge.yokodzun.android.data


import com.sibedge.yokodzun.android.api.API
import ru.hnau.remote_teaching_common.data.User


object MeInfoManager : RTDataManager<User>(
    invalidateAfterUserLogin = true,
    valueLifetime = null
) {

    override suspend fun getValue() =
        API.me().await()

}
package com.sibedge.yokodzun.android.utils.extensions

import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import com.sibedge.yokodzun.android.R
import ru.hnau.remote_teaching_common.exception.ApiException
import ru.hnau.remote_teaching_common.exception.ApiExceptionContent


val ApiException.text: StringGetter
    get() {
        (content as? ApiExceptionContent.Common)?.let {
            return it.message.toGetter()
        }

        //TODO handle custom contents

        return StringGetter(R.string.error_undefined)
    }